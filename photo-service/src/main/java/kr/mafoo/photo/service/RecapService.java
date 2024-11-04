package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.PhotoEntity;
import kr.mafoo.photo.service.properties.RecapProperties;
import kr.mafoo.photo.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.FFmpegExecutor;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecapService {

    @Value("${recap.max-size}")
    private int recapImageMaxSize;

    @Value("${recap.path.tmp}")
    private String tmpPath;

    private final AlbumService albumService;
    private final PhotoService photoService;
    private final MemberService memberService;

    private final ObjectStorageService objectStorageService;
    private final Graphics2dService graphics2dService;
    private final FFmpegExecutor ffmpegExecutor;
    private final LocalFileService localFileService;

    private final RecapProperties recapProperties;

    public Mono<String> createRecap(String albumId, String requestMemberId, String sort, String token) {

        String recapId = IdGenerator.generate();

        return albumService.findByAlbumId(albumId, requestMemberId)
                .flatMap(albumEntity -> {
                    String albumType = String.valueOf(albumEntity.getType());

                    return graphics2dService.generateAlbumChipForRecap(recapId, albumEntity.getName(), albumType)
                            .then(memberService.getMemberInfo(token))
                            .flatMap(memberInfo -> generateRecapFrame(recapId, memberInfo.name(), albumType))
                            .then(photoService.findAllByAlbumId(albumId, requestMemberId, sort)
                                .collectList()
                                .flatMap(photoEntities -> {
                                    List<String> photoUrls = photoEntities.stream()
                                            .limit(recapImageMaxSize)
                                            .map(PhotoEntity::getPhotoUrl)
                                            .toList();

                                    return objectStorageService.downloadFilesForRecap(photoUrls, recapId);
                                })
                            )
                            .flatMap(downloadedPath -> generateRecapPhotos(downloadedPath, recapId))
                            .then(Mono.defer(() -> generateRecapVideo(recapId)))
                            .flatMap(objectStorageService::uploadFileFromPath);
                })
                .flatMap(recapUploadedPath ->
                        localFileService.deleteSimilarNameFileForPath(tmpPath, recapId)
                                .thenReturn(recapUploadedPath)
                )
                .doOnError(e -> localFileService.deleteSimilarNameFileForPath(tmpPath, recapId));
    }

    private Mono<String> generateRecapFrame(String recapId, String memberName, String albumType) {
        return Mono.fromCallable(() -> {
            try {
                String recapFramePath = recapProperties.getFrameFilePath(recapId);
                String recapCreatedDate = DateTimeFormatter.ofPattern("yyyy.MM.dd").format(LocalDate.now());

                FFmpegBuilder builder = new FFmpegBuilder()
                        .addExtraArgs("-loglevel", "debug")
                        .addExtraArgs(
                                "-filter_complex",
                                String.format(
                                        "[1]scale=w=-1:h=176[chip]; " +
                                        "[0][chip]overlay=188:H-h-120[bg_w_chip]; " +
                                        "[bg_w_chip]drawtext=fontfile=%s:text='@%s님의 RECAP':fontcolor=white@0.7:fontsize=72:x=(w-tw)/2:y=208[bg_w_title]; " +
                                        "[bg_w_title]drawtext=fontfile=%s:text='%s':fontcolor=white:fontsize=72:x=w-tw-188:y=h-th-180",
                                        recapProperties.getAggroBFontPath(), memberName,
                                        recapProperties.getAggroMFontPath(), recapCreatedDate
                                )
                        )
                        .addInput(recapProperties.getBackgroundPath(albumType))
                        .addInput(recapProperties.getChipFilePath(recapId))
                        .addOutput(recapFramePath)
                        .done();

                ffmpegExecutor.createJob(builder).run();

                return recapFramePath;
            } catch (Exception e) {
                log.error("Failed to generate recap frame", e);
                throw new RuntimeException("Failed to generate recap frame", e);
            }
        });
    }

    private Mono<Void> generateRecapPhotos(List<String> downloadedPath, String recapId) {

        return Mono.fromRunnable(() -> {
            try {
                FFmpegBuilder builder = new FFmpegBuilder()
                    .addExtraArgs("-loglevel", "debug")
                    .addInput(recapProperties.getFrameFilePath(recapId));

                for (String path : downloadedPath) {
                    builder.addInput(path);
                }

                StringBuilder filterComplex = new StringBuilder();

                for (int inputIndex = 1; inputIndex <= downloadedPath.size(); inputIndex++) {
                    filterComplex.append(String.format(
                        "[%d]scale='min(1200,iw)':'min(1776,ih)':force_original_aspect_ratio=decrease[photo_scaled_%d]; ",
                        inputIndex, inputIndex));

                    filterComplex.append(String.format(
                        "[0][photo_scaled_%d]overlay=(W-w)/2:(H-h)/2+80[final%d]",
                        inputIndex, inputIndex));

                    if (inputIndex < downloadedPath.size()) {
                        filterComplex.append(";");
                    }
                }

                builder.addExtraArgs("-filter_complex", filterComplex.toString());

                for (int outputIndex = 1; outputIndex <= downloadedPath.size(); outputIndex++) {
                    builder.addOutput(recapProperties.getPhotoFilePath(recapId, outputIndex))
                        .addExtraArgs("-map", String.format("[final%d]", outputIndex));
                }

                ffmpegExecutor.createJob(builder).run();
            } catch (Exception e) {
                log.error("Failed to generate recap photos", e);
                throw new RuntimeException("Failed to generate recap photos", e);
            }
        }).then();
    }

    private Mono<String> generateRecapVideo(String recapId) {
        return Mono.fromCallable(() -> {
            try {
                String recapVideoPath = recapProperties.getVideoFilePath(recapId);

                FFmpegBuilder builder = new FFmpegBuilder()
                        .addExtraArgs("-loglevel", "debug")
                        .addExtraArgs("-r", "2")
                        .addInput(recapProperties.getPhotoFilePath(recapId))
                        .addOutput(recapVideoPath)
                        .done();

                ffmpegExecutor.createJob(builder).run();

                return recapVideoPath;
            } catch (Exception e) {
                log.error("Failed to generate recap video", e);
                throw new RuntimeException("Failed to generate recap video", e);
            }
        });
    }

}
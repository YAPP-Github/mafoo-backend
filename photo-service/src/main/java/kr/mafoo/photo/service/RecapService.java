package kr.mafoo.photo.service;

import kr.mafoo.photo.domain.PhotoEntity;
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

    @Value("${recap.src.background}")
    private String backgroundPath;

    @Value("${recap.src.font.aggro-m}")
    private String fontAggroMPath;

    @Value("${recap.src.font.aggro-b}")
    private String fontAggroBPath;

    @Value("${recap.tmp.dir}")
    private String dirPath;

    @Value("${recap.tmp.file.chip}")
    private String chipPath;

    @Value("${recap.tmp.file.frame}")
    private String framePath;

    @Value("${recap.tmp.file.photo}")
    private String photoPath;

    @Value("${recap.tmp.file.video}")
    private String videoPath;

    private final AlbumService albumService;
    private final PhotoService photoService;

    private final ObjectStorageService objectStorageService;
    private final Graphics2dService graphics2dService;
    private final FFmpegExecutor ffmpegExecutor;
    private final LocalFileService localFileService;

    public Mono<String> createRecap(String albumId, String requestMemberId, String sort) {

        String recapId = IdGenerator.generate();

        return albumService.findByAlbumId(albumId, requestMemberId)
                .flatMap(albumEntity -> {
                    String albumName = albumEntity.getName();
                    String albumType = String.valueOf(albumEntity.getType());

                    // temp
                    String memberName = "시금치파슷하";

                    return graphics2dService.generateAlbumChipForRecap(recapId, albumName, albumType)
                            .then(generateRecapFrame(recapId, memberName, albumType))
                            .then(photoService.findAllByAlbumId(albumId, requestMemberId, sort)
                                .collectList()
                                .flatMap(photoEntities -> {
                                    List<String> photoUrls = photoEntities.stream()
                                            .map(PhotoEntity::getPhotoUrl)
                                            .toList();

                                    return objectStorageService.downloadFilesForRecap(photoUrls, recapId);
                                })
                            )
                            .flatMap(downloadedPath -> generateRecapPhotos(downloadedPath, recapId))
                            .then(Mono.defer(() -> generateRecapVideo(recapId)))
                            .flatMap(objectStorageService::uploadFileFromPath)
                            ;
                })
                .flatMap(recapUploadedPath ->
                        localFileService.deleteSimilarNameFileForPath(dirPath, recapId)
                                .thenReturn(recapUploadedPath)
                );
    }

    private Mono<String> generateRecapFrame(String recapId, String memberName, String albumType) {
        return Mono.fromCallable(() -> {
            try {
                String recapBackgroundPath = String.format(backgroundPath, albumType);
                String recapChipPath = String.format(chipPath, recapId);
                String recapFramePath = String.format(framePath, recapId);
                String recapCreatedDate = DateTimeFormatter.ofPattern("yyyy.MM.dd").format(LocalDate.now());

                FFmpegBuilder builder = new FFmpegBuilder()
                        .addExtraArgs(
                                "-filter_complex",
                                String.format(
                                        "[1]scale=w=-1:h=176[chip]; " +
                                        "[0][chip]overlay=188:H-h-120[bg_w_chip]; " +
                                        "[bg_w_chip]drawtext=fontfile=%s:text='@%s님의 RECAP':fontcolor=white@0.7:fontsize=72:x=(w-tw)/2:y=208[bg_w_title]; " +
                                        "[bg_w_title]drawtext=fontfile=%s:text='%s':fontcolor=white:fontsize=72:x=w-tw-188:y=h-th-180;",
                                        fontAggroBPath, memberName,
                                        fontAggroMPath, recapCreatedDate
                                )
                        )
                        .addInput(recapBackgroundPath)
                        .addInput(recapChipPath)
                        .addOutput(recapFramePath)
                        .done();

                ffmpegExecutor.createJob(builder).run();

                return recapFramePath;
            } catch (Exception e) {
                log.error("Failed to generate recap frame", e);
                throw new RuntimeException("Failed to generate recap_frame", e);
            }
        });
    }

    private Mono<Void> generateRecapPhotos(List<String> downloadedPath, String recapId) {

        return Mono.fromRunnable(() -> {
            String recapFramePath = String.format(framePath, recapId);

            FFmpegBuilder builder = new FFmpegBuilder()
                    .addInput(recapFramePath);

            for (String path : downloadedPath) {
                builder.addInput(path);
            }

            StringBuilder filterComplex = new StringBuilder();

            for (int inputIndex = 1; inputIndex <= downloadedPath.size(); inputIndex++) {
                filterComplex.append(String.format("[%d]scale='min(1200,iw)':'min(1776,ih)':force_original_aspect_ratio=decrease[photo_scaled_%d]; ", inputIndex, inputIndex));
                filterComplex.append(String.format("[recap_bg][photo_scaled_%d]overlay=(W-w)/2:(H-h)/2+80[final%d];", inputIndex, inputIndex));
            }

            builder.addExtraArgs("-filter_complex", filterComplex.toString());

            for (int outputIndex = 1; outputIndex <= downloadedPath.size(); outputIndex++) {
                builder.addOutput(String.format(photoPath, recapId, outputIndex))
                        .addExtraArgs("-map", String.format("[final%d]", outputIndex));
            }

            ffmpegExecutor.createJob(builder).run();
        }).then();
    }

    private Mono<String> generateRecapVideo(String recapId) {
        return Mono.fromCallable(() -> {
            try {
                String recapVideoPath = String.format(videoPath, recapId);

                FFmpegBuilder builder = new FFmpegBuilder()
                        .addExtraArgs("-r", "2")
                        .addInput(photoPath.replace("%s", recapId))
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
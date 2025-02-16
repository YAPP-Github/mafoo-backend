package kr.mafoo.photo.service;

import static kr.mafoo.photo.domain.enums.PermissionLevel.DOWNLOAD_ACCESS;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kr.mafoo.photo.exception.PreSignedUrlBannedFileTypeException;
import kr.mafoo.photo.exception.PreSignedUrlExceedMaximumException;
import kr.mafoo.photo.util.RecapProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObjectStorageService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.endpoint}")
    private String endpoint;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.presigned-url-expiration}")
    private long presignedUrlExpiration;

    private final RecapProperties recapProperties;

    private final AlbumPermissionVerifier albumPermissionVerifier;

    public Mono<String> uploadFile(byte[] fileByte) {
        String keyName = "qr/" + UUID.randomUUID() + ".jpeg";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(fileByte.length);
        objectMetadata.setContentType("image/jpeg");

        return Mono.fromCallable(() -> {
            try (InputStream inputStream = new ByteArrayInputStream(fileByte)) {
                amazonS3Client.putObject(
                        new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead));

                return generateFileLink(keyName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image to object storage: ", e);
            }
        });
    }

    public Mono<String> uploadFileFromPath(String filePath) {
        return Mono.fromCallable(() -> {
            File file = new File(filePath);
            String keyName = "recap/" + file.getName();

            if (!file.exists() || !file.isFile()) {
                throw new IllegalArgumentException("Invalid file path: " + filePath);
            }

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.length());
            objectMetadata.setContentType("application/octet-stream");

            try (InputStream inputStream = FileUtils.openInputStream(file)) {
                amazonS3Client.putObject(
                        new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead));

                return generateFileLink(keyName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file to object storage: " + filePath, e);
            }
        });
    }

    public Mono<List<String>> createPreSignedUrls(List<String> fileNames, String memberId) {
        return validateFileCount(fileNames.size(), 30)
            .thenMany(
                Flux.fromIterable(fileNames)
                    .flatMap(fileName -> generatePresignedUrlForImage(fileName, memberId))
            )
            .map(URL::toString)
            .collectList();
    }

    private Mono<URL> generatePresignedUrlForImage(String fileName, String memberId) {
        return extractFileType(fileName)
            .flatMap(fileType -> validateFileType(fileType)
                .then(generatePresignedUrl(
                    generateFilePath(memberId, fileType)
                ))
            );
    }

    private Mono<Void> validateFileType(String fileType) {
        List<String> allowedFileTypes = List.of("jpg", "jpeg", "png");

        return (allowedFileTypes.contains(fileType))
            ? Mono.empty()
            : Mono.error(new PreSignedUrlBannedFileTypeException());
    }

    private String generateFilePath(String memberId, String fileType) {
        return String.format("%s/photo/%s.%s", memberId, UUID.randomUUID(), fileType);
    }

    public Mono<List<String>> createRecapPreSignedUrls(Integer fileCount, String albumId, String requestMemberId) {
        return albumPermissionVerifier.verifyOwnershipOrAccessPermission(albumId, requestMemberId, DOWNLOAD_ACCESS)
            .then(validateFileCount(fileCount, 10)
                .thenMany(Flux.range(0, fileCount))
                .map(i -> generateRecapFilePath(requestMemberId, albumId))
                .flatMap(this::generatePresignedUrl)
                .map(URL::toString)
                .collectList()
            );
    }

    private String generateRecapFilePath(String requestMemberId, String albumId) {
        return String.format("%s/%s/recap/%s.jpeg", requestMemberId, albumId, UUID.randomUUID());
    }

    public Mono<String> setObjectPublicRead(String filePath) {
        String keyName = filePath.split("object.ncloudstorage.com/")[1];

        return Mono.fromCallable(() -> {
            try {
                amazonS3Client.setObjectAcl(bucketName, keyName, CannedAccessControlList.PublicRead);
                return generateFileLink(keyName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to set ACL to PublicRead for the file: " + keyName, e);
            }
        });
    }

    private Mono<String> extractFileType(String fileName) {
        return Mono.just(
                fileName.substring(fileName.lastIndexOf(".") + 1)
        );
    }

    private String generateFileLink(String keyName) {
        return endpoint + "/" + bucketName + "/" + keyName;
    }

    public Mono<List<String>> downloadFilesForRecap(List<String> fileUrls, String recapId) {
        return Mono.defer(() -> {
            try {
                List<String> downloadedPaths = IntStream.range(0, fileUrls.size())
                        .mapToObj(i -> {
                            try {
                                String downloadedPath = recapProperties.getDownloadFilePath(recapId, i+1);
                                FileUtils.copyURLToFile(new URL(fileUrls.get(i)), new File(downloadedPath));

                                return downloadedPath;
                            } catch (IOException e) {
                                throw new RuntimeException("Failed to download image for recap: " + fileUrls.get(i), e);
                            }
                        })
                        .collect(Collectors.toList());

                return Mono.just(downloadedPaths);
            } catch (Exception e) {
                return Mono.error(e);
            }
        });
    }

    private Mono<Void> validateFileCount(int fileCount, int maxAllowed) {
        return fileCount > maxAllowed
            ? Mono.error(new PreSignedUrlExceedMaximumException())
            : Mono.empty();
    }

    private Mono<URL> generatePresignedUrl(String filePath) {
        Date expiration = new Date(System.currentTimeMillis() + presignedUrlExpiration);
        return Mono.just(amazonS3Client.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, filePath)
            .withMethod(HttpMethod.PUT)
            .withExpiration(expiration)));
    }
}

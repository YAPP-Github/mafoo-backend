package kr.mafoo.photo.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kr.mafoo.photo.exception.PreSignedUrlExceedMaximum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.net.URL;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

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

    public Mono<String> uploadFile(byte[] fileByte) {
        String keyName = "qr/" + UUID.randomUUID();

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

    public Mono<String[]> createPreSignedUrls(String[] fileNames, String memberId) {
        return Mono.fromCallable(() -> {
            if (fileNames.length > 30) {
                throw new PreSignedUrlExceedMaximum();
            }

            return Stream.of(fileNames)
                    .map(fileName -> generatePresignedUrl(fileName, memberId).toString())
                    .toArray(String[]::new);
        });
    }

    private URL generatePresignedUrl(String fileName, String memberId) {
        String filePath = String.format("%s/photo/%s_%s", memberId, UUID.randomUUID(), fileName);
        Date expiration = new Date(System.currentTimeMillis() + presignedUrlExpiration);

        return amazonS3Client.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, filePath)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration));
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

    private String generateFileLink(String keyName) {
        return endpoint + "/" + bucketName + "/" + keyName;
    }
}

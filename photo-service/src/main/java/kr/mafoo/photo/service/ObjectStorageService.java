package kr.mafoo.photo.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ObjectStorageService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public Mono<String> uploadFile(byte[] fileByte) {
        String keyName = "/" + UUID.randomUUID();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(fileByte.length);
        objectMetadata.setContentType("image/jpeg");

        return Mono.fromCallable(() -> {
            try (InputStream inputStream = new ByteArrayInputStream(fileByte)) {
                amazonS3Client.putObject(
                        new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead));

                return "https://kr.object.ncloudstorage.com/" + bucketName + "/" + keyName;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


}
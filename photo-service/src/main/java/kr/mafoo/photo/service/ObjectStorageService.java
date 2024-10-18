package kr.mafoo.photo.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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

@Service
@RequiredArgsConstructor
public class ObjectStorageService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.presigned-url-expiration}")
    private long urlExpiration; // Expiration time in milliseconds

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

    public Mono<String []> getPreSignedUrls(String[] fileNames, String memberId) {
        return Mono.fromCallable(() ->
                Stream.of(fileNames)
                        .map(fileName -> generatePresignedUrl(fileName, memberId).toString())
                        .toArray(String[]::new)
        );
    }

    private URL generatePresignedUrl(String fileName, String memberId) {

        String newFileName = UUID.randomUUID() + "_" + fileName;

        String filePath = String.format("%s/photo/%s", memberId, newFileName);

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += urlExpiration; // Use the configured expiration time
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, filePath)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }

}
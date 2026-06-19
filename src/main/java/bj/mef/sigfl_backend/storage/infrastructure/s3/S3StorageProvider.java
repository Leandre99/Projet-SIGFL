package bj.mef.sigfl_backend.storage.infrastructure.s3;

import java.io.InputStream;
import java.time.Duration;
import java.util.Optional;

import bj.mef.sigfl_backend.storage.domain.StorageProvider;
import bj.mef.sigfl_backend.storage.domain.model.FileObject;
import bj.mef.sigfl_backend.storage.infrastructure.StorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

/**
 * {@link StorageProvider} backed by an S3-compatible object store (MinIO, AWS
 * S3, ...). Active when {@code storage.provider=s3}.
 */
@Component
@ConditionalOnProperty(name = "storage.provider", havingValue = "s3")
public class S3StorageProvider implements StorageProvider {

    private final S3Client client;
    private final S3Presigner presigner;
    private final String bucket;

    public S3StorageProvider(S3Client client, S3Presigner presigner, StorageProperties properties) {
        this.client = client;
        this.presigner = presigner;
        this.bucket = properties.getS3().getBucket();
    }

    @Override
    public FileObject store(String key, InputStream content, long size, String contentType) {
        client.putObject(PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(contentType)
                        .contentLength(size)
                        .build(),
                RequestBody.fromInputStream(content, size));
        return new FileObject(key, key, contentType, size);
    }

    @Override
    public Optional<InputStream> retrieve(String key) {
        try {
            return Optional.of(client.getObject(GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build()));
        } catch (NoSuchKeyException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(String key) {
        client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
    }

    @Override
    public boolean exists(String key) {
        try {
            client.headObject(HeadObjectRequest.builder().bucket(bucket).key(key).build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    @Override
    public Optional<String> presignedUrl(String key, Duration ttl) {
        GetObjectRequest getObject = GetObjectRequest.builder().bucket(bucket).key(key).build();
        var presigned = presigner.presignGetObject(GetObjectPresignRequest.builder()
                .signatureDuration(ttl)
                .getObjectRequest(getObject)
                .build());
        return Optional.of(presigned.url().toString());
    }
}

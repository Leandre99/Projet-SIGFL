package org.example.sigfl_backend.storage.domain;

import java.io.InputStream;
import java.time.Duration;
import java.util.Optional;

import org.example.sigfl_backend.storage.domain.model.FileObject;

/**
 * Port for object storage. Implementations are interchangeable adapters
 * (S3/MinIO, local filesystem, ...) selected at runtime via the
 * {@code storage.provider} property. The rest of the application depends only
 * on this interface, never on a concrete provider.
 */
public interface StorageProvider {

    /** Stores {@code content} under {@code key} and returns its metadata. */
    FileObject store(String key, InputStream content, long size, String contentType);

    /** Opens a stream to read the object, or empty if it does not exist. */
    Optional<InputStream> retrieve(String key);

    /** Deletes the object. No-op if it does not exist. */
    void delete(String key);

    /** Whether an object exists for the given key. */
    boolean exists(String key);

    /**
     * Returns a time-limited URL allowing direct download, when the provider
     * supports it (e.g. S3 presigned URLs). Empty otherwise.
     */
    Optional<String> presignedUrl(String key, Duration ttl);
}

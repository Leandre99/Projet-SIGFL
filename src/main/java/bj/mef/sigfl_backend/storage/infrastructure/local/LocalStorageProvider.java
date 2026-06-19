package bj.mef.sigfl_backend.storage.infrastructure.local;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;

import bj.mef.sigfl_backend.storage.domain.StorageProvider;
import bj.mef.sigfl_backend.storage.domain.model.FileObject;
import bj.mef.sigfl_backend.storage.infrastructure.StorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * {@link StorageProvider} that stores files on the local filesystem. Active when
 * {@code storage.provider=local} (the default) — handy for local dev and tests
 * without a running S3/MinIO. Does not support presigned URLs.
 */
@Component
@ConditionalOnProperty(name = "storage.provider", havingValue = "local", matchIfMissing = true)
public class LocalStorageProvider implements StorageProvider {

    private final Path basePath;

    public LocalStorageProvider(StorageProperties properties) {
        this.basePath = Path.of(properties.getLocal().getBasePath()).toAbsolutePath().normalize();
    }

    @Override
    public FileObject store(String key, InputStream content, long size, String contentType) {
        Path target = resolve(key);
        try {
            Files.createDirectories(target.getParent());
            long written = Files.copy(content, target);
            return new FileObject(key, target.getFileName().toString(), contentType, written);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to store file: " + key, e);
        }
    }

    @Override
    public Optional<InputStream> retrieve(String key) {
        Path target = resolve(key);
        if (!Files.exists(target)) {
            return Optional.empty();
        }
        try {
            return Optional.of(Files.newInputStream(target));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read file: " + key, e);
        }
    }

    @Override
    public void delete(String key) {
        try {
            Files.deleteIfExists(resolve(key));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to delete file: " + key, e);
        }
    }

    @Override
    public boolean exists(String key) {
        return Files.exists(resolve(key));
    }

    @Override
    public Optional<String> presignedUrl(String key, Duration ttl) {
        return Optional.empty();
    }

    /** Resolves a key under the base path, rejecting path-traversal attempts. */
    private Path resolve(String key) {
        Path resolved = basePath.resolve(key).normalize();
        if (!resolved.startsWith(basePath)) {
            throw new IllegalArgumentException("Invalid storage key: " + key);
        }
        return resolved;
    }
}

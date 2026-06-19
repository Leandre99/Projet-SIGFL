package bj.mef.sigfl_backend.storage.application;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import bj.mef.sigfl_backend.storage.domain.StorageProvider;
import bj.mef.sigfl_backend.storage.domain.model.FileObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Application service for file storage. Orchestrates the active
 * {@link StorageProvider} adapter and owns app-level concerns such as key
 * generation, so callers never depend on a concrete provider.
 */
@Service
public class StorageService {

    private final StorageProvider provider;

    public StorageService(StorageProvider provider) {
        this.provider = provider;
    }

    /** Stores an uploaded multipart file under a generated key. */
    public FileObject upload(MultipartFile file) {
        String key = generateKey(file.getOriginalFilename());
        try (InputStream in = file.getInputStream()) {
            return provider.store(key, in, file.getSize(), file.getContentType());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read uploaded file", e);
        }
    }

    public Optional<InputStream> download(String key) {
        return provider.retrieve(key);
    }

    public void delete(String key) {
        provider.delete(key);
    }

    public Optional<String> presignedUrl(String key, Duration ttl) {
        return provider.presignedUrl(key, ttl);
    }

    private String generateKey(String originalFilename) {
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        return UUID.randomUUID() + suffix;
    }
}

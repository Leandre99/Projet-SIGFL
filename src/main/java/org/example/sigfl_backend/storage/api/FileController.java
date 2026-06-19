package org.example.sigfl_backend.storage.api;

import java.io.InputStream;

import org.example.sigfl_backend.storage.application.StorageService;
import org.example.sigfl_backend.storage.domain.model.FileObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Sample REST slice for the storage module: upload / download / delete against
 * whichever provider is active. Use it as the template for real resource APIs.
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final StorageService storage;

    public FileController(StorageService storage) {
        this.storage = storage;
    }

    @PostMapping
    public FileObject upload(@RequestParam("file") MultipartFile file) {
        return storage.upload(file);
    }

    @GetMapping("/{key}")
    public ResponseEntity<Resource> download(@PathVariable String key) {
        return storage.download(key)
                .map(this::toResponse)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> delete(@PathVariable String key) {
        storage.delete(key);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Resource> toResponse(InputStream stream) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(stream));
    }
}

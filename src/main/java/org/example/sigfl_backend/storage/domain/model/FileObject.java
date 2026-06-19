package org.example.sigfl_backend.storage.domain.model;

/**
 * Metadata describing a stored file, independent of the backing provider.
 *
 * @param key         provider-side identifier (object key / relative path)
 * @param filename    original file name
 * @param contentType MIME type
 * @param size        size in bytes
 */
public record FileObject(String key, String filename, String contentType, long size) {
}

package bj.mef.sigfl_backend.storage.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Storage configuration, bound from the {@code storage.*} properties.
 *
 * <p>Switch provider with {@code storage.provider} (env {@code STORAGE_PROVIDER}):
 * <ul>
 *   <li>{@code s3}    — any S3-compatible backend (MinIO, AWS S3, ...), see {@link S3} below;</li>
 *   <li>{@code local} — local filesystem under {@link Local#basePath}.</li>
 * </ul>
 */
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    /** Active provider: {@code s3} or {@code local}. */
    private String provider = "local";

    private final S3 s3 = new S3();
    private final Local local = new Local();

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public S3 getS3() {
        return s3;
    }

    public Local getLocal() {
        return local;
    }

    /** S3 / MinIO settings. {@code endpoint} blank = real AWS S3. */
    public static class S3 {
        private String endpoint;
        private String region = "us-east-1";
        private String accessKey;
        private String secretKey;
        private String bucket;
        /** Required for MinIO and most non-AWS S3 implementations. */
        private boolean pathStyleAccess = true;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public boolean isPathStyleAccess() {
            return pathStyleAccess;
        }

        public void setPathStyleAccess(boolean pathStyleAccess) {
            this.pathStyleAccess = pathStyleAccess;
        }
    }

    /** Local filesystem settings. */
    public static class Local {
        private String basePath = "./data/storage";

        public String getBasePath() {
            return basePath;
        }

        public void setBasePath(String basePath) {
            this.basePath = basePath;
        }
    }
}

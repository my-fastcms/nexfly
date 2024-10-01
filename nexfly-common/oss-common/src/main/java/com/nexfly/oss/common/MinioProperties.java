package com.nexfly.oss.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author wangjun
 * @Date 2024/9/28
 **/
@ConfigurationProperties(MinioProperties.MINIO_PREFIX)
public class MinioProperties extends OssProperties {

    public static final String MINIO_PREFIX = OSS_PREFIX + ".minio";

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

}

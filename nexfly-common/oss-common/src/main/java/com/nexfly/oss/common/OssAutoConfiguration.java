package com.nexfly.oss.common;

import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author wangjun
 * @Date 2024/9/28
 **/
@Configuration
@EnableConfigurationProperties({ MinioProperties.class })
public class OssAutoConfiguration {

    @Bean
    public MinioClient minioClient(MinioProperties minioProperties) {
        return MinioClient.builder().endpoint(minioProperties.getEndpoint()).credentials(minioProperties.getAccessKeyId(), minioProperties.getAccessKeySecret()).build();
    }

}

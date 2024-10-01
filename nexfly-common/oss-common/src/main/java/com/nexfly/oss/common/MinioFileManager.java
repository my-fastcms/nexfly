package com.nexfly.oss.common;

import com.nexfly.common.core.exception.NexflyException;
import io.minio.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @Author wangjun
 * @Date 2024/9/28
 **/
@Service
@Primary
public class MinioFileManager implements OssFileManager, InitializingBean {

    private static final String DEFAULT_BUCKET = "nexfly";

    @Autowired
    private MinioClient minioClient;

    @Override
    public String upload(String fileName, String fileContentType, InputStream inputStream) throws NexflyException {
        ObjectWriteResponse objectWriteResponse;
        try {
            objectWriteResponse = minioClient.putObject(
                    PutObjectArgs.builder().bucket(DEFAULT_BUCKET).object(fileName).stream(inputStream, inputStream.available(), -1).contentType(fileContentType).build());
        } catch (Exception e) {
            throw new NexflyException(e.getMessage());
        }
        return objectWriteResponse.object();
    }

    @Override
    public InputStream download(String filePath) throws NexflyException {
        try {
            StatObjectResponse statObjectResponse = minioClient.statObject(StatObjectArgs.builder().bucket(DEFAULT_BUCKET).object(filePath).build());
            return minioClient.getObject(GetObjectArgs.builder().bucket(DEFAULT_BUCKET).object(filePath).build());
        } catch (Exception e) {
            throw new NexflyException(e.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket(DEFAULT_BUCKET).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(DEFAULT_BUCKET).build());
        }
    }

}

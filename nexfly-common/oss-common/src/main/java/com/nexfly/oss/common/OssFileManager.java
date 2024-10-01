package com.nexfly.oss.common;

import com.nexfly.common.core.exception.NexflyException;

import java.io.InputStream;

/**
 * @Author wangjun
 * @Date 2024/9/28
 **/
public interface OssFileManager {

    String upload(String fileName, String fileContentType, InputStream inputStream) throws NexflyException;

    InputStream download(String filePath) throws NexflyException;

}

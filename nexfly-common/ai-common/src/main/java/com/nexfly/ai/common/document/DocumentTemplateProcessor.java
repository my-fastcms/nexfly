package com.nexfly.ai.common.document;

import org.springframework.ai.document.Document;

import java.io.InputStream;
import java.util.List;

/**
 * 文档处理器
 * @Author wangjun
 * @Date 2024/10/17
 **/
public interface DocumentTemplateProcessor {

    List<Document> process(InputStream inputStream, String fileType) throws Exception;

}

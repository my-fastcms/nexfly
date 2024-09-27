package com.nexfly.system.service;

import com.nexfly.common.core.exception.NexflyException;
import com.nexfly.system.model.Document;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/8/25
 **/
public interface DocumentService {

    List<Document> list(Long datasetId);

    void saveDocument(DocumentParam documentParam) throws NexflyException;

    record DocumentParam(@NotNull Long orgId,
                         @NotNull Long datasetId,
                         @NotNull Long documentId,
                         @NotNull String name,
                         @NotNull String content,
                         @NotNull String keywords) {

    }

}

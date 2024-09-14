package com.nexfly.system.service;

import com.nexfly.common.core.exception.NexflyException;
import org.jetbrains.annotations.NotNull;

/**
 * @Author wangjun
 * @Date 2024/8/25
 **/
public interface DocumentService {

    void saveDocument(DocumentParam documentParam) throws NexflyException;

    record DocumentParam(@NotNull Long orgId,
                         @NotNull Long datasetId,
                         @NotNull Long documentId,
                         @NotNull String name,
                         @NotNull String content,
                         @NotNull String keywords) {

    }

}

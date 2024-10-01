package com.nexfly.system.service;

import com.nexfly.common.core.exception.NexflyException;
import com.nexfly.system.model.Document;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/8/25
 **/
public interface DocumentService {

    List<Document> list(Long datasetId);

    void uploadDocument(UploadRequest uploadRequest) throws NexflyException;

    void processDocument(AnalysisRequest analysisRequest) throws Exception;

    record UploadRequest(@NotNull Long datasetId, @NotNull String fileName, @NotNull String fileContentType, Long fileSize, @NotNull InputStream inputStream) {

    }

    record AnalysisRequest(List<Long> documentIds, Integer run) {

    }

    /**
     * 文档数据来源
     */
    enum SourceType {

        /**
         * 本地上传
         */
        LOCAL(1),

        /**
         * 网络爬取
         */
        WEB_CRAWLER(2);

        public final Integer value;

        SourceType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return this.value;
        }

    }

    /**
     * 文档处理方式
     */
    enum ProcessType {
        GENERAL("naive"),
        QA("qa"),
        RESUME("resume"),
        MANUAL("manual"),
        TABLE("table"),
        PAPER("paper"),
        BOOK("book"),
        LAWS("laws"),
        PRESENTATION("presentation"),
        PICTURE("picture"),
        ONE("one"),
        AUDIO("audio"),
        KNOWLEDGE_GRAPH("knowledge_graph"),
        EMAIL("email");

        public final String value;

        ProcessType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    /**
     * 文档处理状态
     */
    enum ProcessStatus {
        /**
         * 未处理
         */
        UNSTART(0),

        /**
         * 正在处理
         */
        RUNNING(1),

        /**
         * 取消
         */
        CANCEL(2),

        /**
         * 已完成
         */
        DONE(3),

        /**
         * 处理失败
         */
        FAIL(4);

        public final Integer value;

        ProcessStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return this.value;
        }
    }

}

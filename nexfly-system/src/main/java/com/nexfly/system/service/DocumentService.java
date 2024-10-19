package com.nexfly.system.service;

import com.github.pagehelper.PageInfo;
import com.nexfly.common.core.exception.NexflyException;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/8/25
 **/
public interface DocumentService {

    PageInfo<DocumentResponse> list(Long datasetId, Integer page, Integer pageSize);

    void uploadDocument(UploadRequest uploadRequest) throws NexflyException;

    void processDocument(AnalysisRequest analysisRequest) throws Exception;

    void processSingleDocument(Long orgId, Long documentId) throws Exception;

    void changeStatus(ChangeStatusRequest changeStatusRequest) throws NexflyException;

    void renameDocument(RenameRequest renameRequest) throws NexflyException;

    InputStream downloadDocument(Long documentId) throws NexflyException;

    void delete(DeleteDocumentRequest deleteDocumentRequest);

    record UploadRequest(@NotNull Long datasetId, @NotNull String fileName, @NotNull String fileContentType, Long fileSize, @NotNull InputStream inputStream) {

    }

    record DocumentResponse(Long documentId, Long orgId, Long datasetId, Long fileId, String name, Integer dataSource,
                            String dataUrl, String processType, Integer processStatus, String parserConfig, Date createAt, Integer status, Long chuckNum) {

    }

    record AnalysisRequest(List<Long> documentIds, Integer run) {

    }

    record ChangeStatusRequest(Long documentId, Integer status) {

    }

    record RenameRequest(Long documentId, String name) {

    }

    record DeleteDocumentRequest(Long datasetId, List<Long> documentIds) {

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
     * | **Template** | Description                                                  | File format                                          |
     * | ------------ | ------------------------------------------------------------ | ---------------------------------------------------- |
     * | General      | Files are consecutively chunked based on a preset chunk token number. | DOCX, EXCEL, PPT, PDF, TXT, JPEG, JPG, PNG, TIF, GIF |
     * | Q&A          |                                                              | EXCEL, CSV/TXT                                       |
     * | Manual       |                                                              | PDF                                                  |
     * | Table        |                                                              | EXCEL, CSV/TXT                                       |
     * | Paper        |                                                              | PDF                                                  |
     * | Book         |                                                              | DOCX, PDF, TXT                                       |
     * | Laws         |                                                              | DOCX, PDF, TXT                                       |
     * | Presentation |                                                              | PDF, PPTX                                            |
     * | Picture      |                                                              | JPEG, JPG, PNG, TIF, GIF                             |
     * | One          | The entire document is chunked as one.                       | DOCX, EXCEL, PDF, TXT                                |
     * 文档处理方式
     */
    enum ProcessType {
        GENERAL("naive"),

        /**
         * 问答型
         */
        QA("qa"),

        /**
         * 简历
         */
        RESUME("resume"),

        /**
         * 手册
         */
        MANUAL("manual"),

        /**
         * EXCEL 和 CSV/TXT，这些格式通常用于存储结构化的表格数据，如数据表或清单
         */
        TABLE("table"),

        /**
         * 论文
         */
        PAPER("paper"),

        /**
         * 书籍 DOCX、PDF、TXT，说明这个模板能够处理 Word 文档、PDF 和纯文本文件的书籍内容
         */
        BOOK("book"),

        /**
         * 法律文件
         */
        LAWS("laws"),

        /**
         * 演示文稿
         */
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

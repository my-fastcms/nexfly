package com.nexfly.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nexfly.ai.common.vectorstore.VectorStoreManager;
import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.exception.NexflyException;
import com.nexfly.common.core.utils.ApplicationUtils;
import com.nexfly.oss.common.OssFileManager;
import com.nexfly.system.manager.ModelManager;
import com.nexfly.system.mapper.AttachmentMapper;
import com.nexfly.system.mapper.DatasetMapper;
import com.nexfly.system.mapper.DocumentMapper;
import com.nexfly.system.mapper.DocumentSegmentMapper;
import com.nexfly.system.model.Attachment;
import com.nexfly.system.model.Dataset;
import com.nexfly.system.model.Document;
import com.nexfly.system.model.DocumentSegment;
import com.nexfly.system.service.AttachmentService;
import com.nexfly.system.service.DocumentService;
import com.nexfly.system.service.SystemService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author wangjun
 * @Date 2024/8/25
 **/
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DatasetMapper datasetMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private SystemService systemService;

    @Autowired
    private VectorStoreManager vectorStoreManager;

    @Autowired
    private ModelManager modelManager;

    @Autowired
    private DocumentSegmentMapper documentSegmentMapper;

    @Autowired
    private OssFileManager ossFileManager;

    @Autowired
    private AttachmentService attachmentService;

    @Override
    public PageInfo<DocumentResponse> list(Long datasetId, Integer page, Integer pageSize) {
        Dataset dataset = datasetMapper.findById(datasetId);

        PageInfo<Document> documentPage = PageHelper.startPage(page, pageSize).doSelectPageInfo(() -> documentMapper.findDocumentListByDatasetId(datasetId));
        List<DocumentResponse> responseList = documentPage.getList().stream()
                .map(document -> new DocumentResponse(document.getDocumentId(), document.getOrgId(), document.getDatasetId(), document.getFileId(), document.getName(),
                        document.getDataSource(), document.getDataUrl(), document.getProcessType(), document.getProcessStatus(), dataset.getParserConfig(), document.getCreateAt(), document.getStatus(),
                        documentSegmentMapper.getCountByDocumentId(document.getDocumentId())))
                .toList();
        PageInfo<DocumentService.DocumentResponse> responsePage = new PageInfo<>(responseList);
        responsePage.setPageNum(documentPage.getPageNum());
        responsePage.setPageSize(documentPage.getPageSize());
        responsePage.setTotal(documentPage.getTotal());
        responsePage.setPages(documentPage.getPages());
        return responsePage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadDocument(UploadRequest uploadRequest) throws NexflyException {
        Dataset dataset = datasetMapper.findById(uploadRequest.datasetId());

        if (dataset == null) {
            throw new NexflyException("dataset不存在");
        }
        Attachment attachment = attachmentService.upload(uploadRequest.fileName(), uploadRequest.fileSize(), uploadRequest.fileContentType(), uploadRequest.inputStream());

        Document doc = getDocument(attachment, systemService.getOrgId(AuthUtils.getUserId()), dataset);
        documentMapper.save(doc);
    }

    @NotNull
    private Document getDocument(Attachment attachment, Long orgId, Dataset dataset) {
        Document doc = new Document();
        doc.setName(attachment.getFileName());
        doc.setOrgId(orgId);
        doc.setFileId(attachment.getAttachmentId());
        doc.setDatasetId(dataset.getDatasetId());
        doc.setDataSource(SourceType.LOCAL.getValue());
        doc.setProcessStatus(ProcessStatus.UNSTART.getValue());
        doc.setProcessType(ProcessType.GENERAL.getValue());
        doc.setStatus(NexflyConstants.Status.NORMAL.getValue());
        return doc;
    }

    @Override
    public void processDocument(AnalysisRequest analysisRequest) throws Exception {
        Long orgId = systemService.getOrgId(AuthUtils.getUserId());
        for (Long documentId : analysisRequest.documentIds()) {
            ApplicationUtils.getBean(DocumentService.class).processSingleDocument(orgId, documentId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processSingleDocument(Long orgId, Long documentId) throws Exception {
        // 从minio下载文件并进行分割
        Document doc = documentMapper.findById(documentId);
        Dataset dataset = datasetMapper.findById(doc.getDatasetId());
        Attachment attachment = attachmentMapper.findById(doc.getFileId());
        try (InputStream inputStream = ossFileManager.download(attachment.getPath())) {
            List<org.springframework.ai.document.Document> documents;
            if (attachment.getType().equalsIgnoreCase("md")) {
                documents = new MarkdownDocumentReader(new InputStreamResource(inputStream), MarkdownDocumentReaderConfig.builder().build()).get();
            } else if (attachment.getType().equalsIgnoreCase("pdf")) {
                documents = new PagePdfDocumentReader(new InputStreamResource(inputStream)).get();
            } else {
                documents = new TikaDocumentReader(new InputStreamResource(inputStream)).get();
            }

            var tokenTextSplitter = new TokenTextSplitter();
            List<org.springframework.ai.document.Document> splitDocuments = tokenTextSplitter.apply(documents);
            // 插入向量数据库
            EmbeddingModel embeddingModel = modelManager.getEmbeddingModel(dataset.getDatasetId());
            VectorStore vectorStore = vectorStoreManager.getVectorStoreFactory().getVectorStore(embeddingModel);

            // 如果向量数据库存在，先删除
            List<DocumentSegment> segmentList = documentSegmentMapper.getListByDocumentId(doc.getDocumentId());
            List<String> contentIdList = segmentList.stream().map(DocumentSegment::getContentId).toList();
            if (!contentIdList.isEmpty()) {
                vectorStore.delete(contentIdList);
                documentSegmentMapper.deleteByDocumentIds(Arrays.stream(new Long[] {doc.getDocumentId()}).toList());
            }

            List<NexflyDocument> list = splitDocuments.stream().map(d -> new NexflyDocument(documentId, orgId, dataset.getDatasetId(), d.getId(), d.getContent())).toList();
            List<org.springframework.ai.document.Document> documentList = list.stream().map(d -> (org.springframework.ai.document.Document) d).toList();
            vectorStore.add(documentList);

            List<DocumentSegment> documentSegmentList = splitDocuments.stream().map(document -> {
                DocumentSegment documentSegment = new DocumentSegment();
                documentSegment.setOrgId(orgId);
                documentSegment.setDatasetId(doc.getDatasetId());
                documentSegment.setContentId(document.getId());
                documentSegment.setDocumentId(doc.getDocumentId());
                documentSegment.setContent(document.getContent());
                documentSegment.setStatus(NexflyConstants.Status.NORMAL.getValue());
                return documentSegment;
            }).toList();

            if (!documentSegmentList.isEmpty()) {
                documentSegmentMapper.insertBatch(documentSegmentList);
            }

            doc.setProcessStatus(ProcessStatus.DONE.value);
            documentMapper.update(doc);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void changeStatus(ChangeStatusRequest changeStatusRequest) throws NexflyException {
        Document document = documentMapper.findById(changeStatusRequest.documentId());
        if (document == null) {
            throw new NexflyException("文档不存在");
        }

        document.setStatus(changeStatusRequest.status());
        documentMapper.update(document);
    }

    @Override
    public void renameDocument(RenameRequest renameRequest) throws NexflyException {
        if (StringUtils.isBlank(renameRequest.name())) {
            throw new NexflyException("名称不能为空");
        }

        Document document = documentMapper.findById(renameRequest.documentId());
        if (document == null) {
            throw new NexflyException("文档不存在");
        }

        document.setName(renameRequest.name());
        documentMapper.update(document);
    }

    @Override
    public InputStream downloadDocument(Long documentId) throws NexflyException {
        Document document = documentMapper.findById(documentId);
        Attachment attachment = attachmentMapper.findById(document.getFileId());
        return ossFileManager.download(attachment.getPath());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(DeleteDocumentRequest deleteDocumentRequest) {
        // 删除向量数据库数据
        try {
            VectorStore vectorStore = getVectorStore(deleteDocumentRequest.datasetId());
            for (Long documentId : deleteDocumentRequest.documentIds()) {
                List<DocumentSegment> documentSegmentList = documentSegmentMapper.getListByDocumentId(documentId);
                List<String> list = documentSegmentList.stream().map(DocumentSegment::getContentId).toList();
                vectorStore.delete(list);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        documentSegmentMapper.deleteByDocumentIds(deleteDocumentRequest.documentIds());
        documentMapper.deleteByDocumentIds(deleteDocumentRequest.documentIds());
    }

    VectorStore getVectorStore(Long datasetId) throws Exception {
        EmbeddingModel embeddingModel = modelManager.getEmbeddingModel(datasetId);
        return vectorStoreManager.getVectorStoreFactory().getVectorStore(embeddingModel);
    }

    private String getFileNameSuffix(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

    static final class NexflyDocument extends org.springframework.ai.document.Document {

        private Long documentId;

        private Long orgId;

        private Long datasetId;

        private float[] embedding;

        private Date createAt;

        private Date updateAt;

        public NexflyDocument(Long documentId, Long orgId, Long datasetId, String id, String content) {
            super(id, content, Map.of());
            this.documentId = documentId;
            this.orgId = orgId;
            this.datasetId = datasetId;
            setCreateAt(new Date());
            setUpdateAt(new Date());
        }

        public Long getDocumentId() {
            return documentId;
        }

        public void setDocumentId(Long documentId) {
            this.documentId = documentId;
        }

        public Long getOrgId() {
            return orgId;
        }

        public void setOrgId(Long orgId) {
            this.orgId = orgId;
        }

        public Long getDatasetId() {
            return datasetId;
        }

        public void setDatasetId(Long datasetId) {
            this.datasetId = datasetId;
        }

        public float[] getEmbedding() {
            return this.embedding;
        }

        public void setEmbedding(float[] embedding) {
            Assert.notNull(embedding, "embedding must not be null");
            this.embedding = embedding;
        }

        public Date getCreateAt() {
            return createAt;
        }

        public void setCreateAt(Date createAt) {
            this.createAt = createAt;
        }

        public Date getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(Date updateAt) {
            this.updateAt = updateAt;
        }

        @Override
        public String toString() {
            return "NexflyDocument{" +
                    "contentId=" + getId() +
                    "documentId=" + documentId +
                    ", orgId=" + orgId +
                    ", datasetId=" + datasetId +
                    ", createAt=" + createAt +
                    ", updateAt=" + updateAt +
                    '}';
        }

    }

}

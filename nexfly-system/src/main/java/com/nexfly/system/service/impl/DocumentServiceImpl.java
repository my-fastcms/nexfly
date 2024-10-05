package com.nexfly.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nexfly.ai.common.vectorstore.VectorStoreManager;
import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.common.core.exception.NexflyException;
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
import com.nexfly.system.service.DocumentService;
import com.nexfly.system.service.SystemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

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
        String object = ossFileManager.upload(uploadRequest.fileName(), uploadRequest.fileContentType(), uploadRequest.inputStream());

        Long orgId = systemService.getOrgId(AuthUtils.getUserId());
        Attachment attachment = new Attachment();
        attachment.setOrgId(orgId);
        attachment.setFileName(uploadRequest.fileName());
        attachment.setPath(object);
        attachment.setSize(uploadRequest.fileSize());
        attachment.setType(Objects.requireNonNull(uploadRequest.fileName()).substring(uploadRequest.fileName().lastIndexOf(".") + 1));
        attachmentMapper.save(attachment);

        com.nexfly.system.model.Document doc = new com.nexfly.system.model.Document();
        doc.setName(attachment.getFileName());
        doc.setOrgId(orgId);
        doc.setFileId(attachment.getAttachmentId());
        doc.setDatasetId(dataset.getDatasetId());
        doc.setDataSource(SourceType.LOCAL.getValue());
        doc.setProcessStatus(ProcessStatus.UNSTART.getValue());
        doc.setProcessType(ProcessType.GENERAL.value);
        documentMapper.save(doc);
    }

    @Override
    public void processDocument(AnalysisRequest analysisRequest) throws Exception {
        Long orgId = systemService.getOrgId(AuthUtils.getUserId());
        for (Long documentId : analysisRequest.documentIds()) {
            Document doc = documentMapper.findById(documentId);
            Attachment attachment = attachmentMapper.findById(doc.getFileId());
            InputStream inputStream = ossFileManager.download(attachment.getPath());
            List<org.springframework.ai.document.Document> documents = new TikaDocumentReader(new InputStreamResource(inputStream)).get();
            var tokenTextSplitter = new TokenTextSplitter(400, 5, 200, 10000, true);
            List<org.springframework.ai.document.Document> splitDocuments = tokenTextSplitter.apply(documents);

            // 插入向量数据库
            Dataset dataset = datasetMapper.findById(doc.getDatasetId());
            EmbeddingModel embeddingModel = modelManager.getEmbeddingModel(dataset.getDatasetId());
            VectorStore vectorStore = vectorStoreManager.getVectorStoreFactory().getVectorStore(dataset.getVsIndexNodeId(), embeddingModel);
            vectorStore.add(splitDocuments);

            List<DocumentSegment> documentSegmentList = splitDocuments.stream().map(document -> {
                DocumentSegment documentSegment = new DocumentSegment();
                documentSegment.setOrgId(orgId);
                documentSegment.setDatasetId(doc.getDatasetId());
                documentSegment.setContentId(document.getId());
                documentSegment.setDocumentId(doc.getDocumentId());
                documentSegment.setContent(document.getContent());
                return documentSegment;
            }).toList();

            if (!documentSegmentList.isEmpty()) {
                documentSegmentMapper.insertBatch(documentSegmentList);
            }

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
    public void delete(List<Long> documentIds) {
        documentSegmentMapper.deleteByDocumentIds(documentIds);
        documentMapper.deleteByDocumentIds(documentIds);
    }

}

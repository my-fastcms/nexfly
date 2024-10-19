package com.nexfly.system.service.impl;

import com.nexfly.ai.common.vectorstore.VectorStoreManager;
import com.nexfly.system.manager.ModelManager;
import com.nexfly.system.mapper.DocumentMapper;
import com.nexfly.system.mapper.DocumentSegmentMapper;
import com.nexfly.system.model.Document;
import com.nexfly.system.model.DocumentSegment;
import com.nexfly.system.service.SegmentService;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author wangjun
 * @Date 2024/9/29
 **/
@Service
public class SegmentServiceImpl implements SegmentService {

    @Autowired
    private DocumentSegmentMapper documentSegmentMapper;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private ModelManager modelManager;

    @Autowired
    private VectorStoreManager vectorStoreManager;

    @Override
    public DocumentSegment getById(Long segmentId) {
        return documentSegmentMapper.findById(segmentId);
    }

    @Override
    public List<DocumentSegment> list(SegmentListRequest segmentListRequest) {
        return documentSegmentMapper.getListByDocumentId(segmentListRequest.documentId());
    }

    @Override
    public RetrievalTestResponse retrievalTest(RetrievalTestRequest retrievalTestRequest) {
        return new RetrievalTestResponse(new ArrayList<>(), new ArrayList<>(), 0);
    }

    @Override
    public void update(UpdateSegmentRequest updateSegmentRequest) {

        DocumentSegment segment = documentSegmentMapper.findById(updateSegmentRequest.segmentId());
        String contentId = segment.getContentId();
        try {
            VectorStore vectoryStore = getVectorStore(segment.getDatasetId());
            vectoryStore.delete(Arrays.stream(new String[] { contentId }).toList());

            org.springframework.ai.document.Document document = new org.springframework.ai.document.Document(contentId, updateSegmentRequest.content(), Map.of());
            vectoryStore.add(Arrays.stream(new org.springframework.ai.document.Document[] { document }).toList());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        DocumentSegment documentSegment = new DocumentSegment();
        documentSegment.setSegmentId(updateSegmentRequest.segmentId());
        documentSegment.setDocumentId(updateSegmentRequest.documentId());
        documentSegment.setContent(updateSegmentRequest.content());
        documentSegment.setKeywords(updateSegmentRequest.keywords());
        documentSegmentMapper.update(documentSegment);
    }

    @Override
    public void delete(DeleteSegmentRequest deleteSegmentRequest) {
        deleteFromVectorStore(deleteSegmentRequest.documentId(), deleteSegmentRequest.segmentIds());
        documentSegmentMapper.deleteBySegmentIds(deleteSegmentRequest.documentId(), deleteSegmentRequest.segmentIds());
    }

    @Override
    public void switchEnable(SwitchEnableRequest switchEnableRequest) {
        deleteFromVectorStore(switchEnableRequest.documentId(), switchEnableRequest.segmentIds());
        documentSegmentMapper.switchEnableBatch(switchEnableRequest.documentId(), switchEnableRequest.segmentIds(), switchEnableRequest.status());
    }

    void deleteFromVectorStore(Long documentId, List<Long> segmentIds) {
        Document document = documentMapper.findById(documentId);

        List<DocumentSegment> documentSegmentList =  documentSegmentMapper.getListByDocumentId(documentId);
        List<String> list = documentSegmentList.stream().filter(item -> segmentIds.contains(item.getSegmentId())).map(DocumentSegment::getContentId).toList();

        if (!list.isEmpty()) {
            try {
                VectorStore vectorStore = getVectorStore(document.getDatasetId());
                vectorStore.delete(list);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    VectorStore getVectorStore(Long datasetId) throws Exception {
        EmbeddingModel embeddingModel = modelManager.getEmbeddingModel(datasetId);
        return vectorStoreManager.getVectorStoreFactory().getVectorStore(embeddingModel);
    }

}

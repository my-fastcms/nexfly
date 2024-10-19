package com.nexfly.system.service;

import com.nexfly.system.model.DocumentSegment;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/9/29
 **/
public interface SegmentService {

    DocumentSegment getById(Long segmentId);

    List<DocumentSegment> list(SegmentListRequest segmentListRequest);

    RetrievalTestResponse retrievalTest(RetrievalTestRequest retrievalTestRequest);

    void update(UpdateSegmentRequest updateSegmentRequest);

    void delete(DeleteSegmentRequest deleteSegmentRequest);

    void switchEnable(SwitchEnableRequest switchEnableRequest);

    record SegmentListRequest(Long documentId, Integer page, @DefaultValue("10") Integer pageSize) {

    }

    record RetrievalTestRequest(Long datasetId, Long [] documentIds, Integer page, String question, String similarityThreshold, Integer size, String vectorSimilarityWeight) {

    }

    record RetrievalTestResponse(List<Object> chunks, List<Object> documents, Integer total) {

    }

    record UpdateSegmentRequest(Long documentId, Long segmentId, String content, String keywords) {

    }

    record SwitchEnableRequest(Long documentId, List<Long> segmentIds, Integer status) {

    }

    record DeleteSegmentRequest(Long documentId, List<Long> segmentIds) {

    }

}

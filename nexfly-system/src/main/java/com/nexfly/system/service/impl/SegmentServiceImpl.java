package com.nexfly.system.service.impl;

import com.nexfly.system.mapper.DocumentSegmentMapper;
import com.nexfly.system.model.DocumentSegment;
import com.nexfly.system.service.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/9/29
 **/
@Service
public class SegmentServiceImpl implements SegmentService {

    @Autowired
    private DocumentSegmentMapper documentSegmentMapper;

    @Override
    public List<DocumentSegment> list(SegmentListRequest segmentListRequest) {
        return documentSegmentMapper.getListByDocumentId(segmentListRequest.documentId());
    }

    @Override
    public RetrievalTestResponse retrievalTest(RetrievalTestRequest retrievalTestRequest) {
        return new RetrievalTestResponse(new ArrayList<>(), new ArrayList<>(), 10);
    }

}

package com.nexfly.system.service;

import com.nexfly.system.model.DocumentSegment;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/9/29
 **/
public interface SegmentService {

    List<DocumentSegment> list(SegmentListRequest segmentListRequest);

    record SegmentListRequest(Long documentId, Integer page, @DefaultValue("10") Integer pageSize) {

    }

}
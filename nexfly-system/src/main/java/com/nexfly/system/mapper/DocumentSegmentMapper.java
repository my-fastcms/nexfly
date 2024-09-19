package com.nexfly.system.mapper;

import com.nexfly.system.model.DocumentSegment;
import org.apache.ibatis.annotations.Param;

public interface DocumentSegmentMapper {
    DocumentSegment findById(@Param("segmentId") Long segmentId);

    void save(@Param("documentSegment") DocumentSegment DocumentSegment);

    void update(@Param("documentSegment") DocumentSegment DocumentSegment);

    Long getCountByDatasetId(@Param("datasetId") Long datasetId);

}
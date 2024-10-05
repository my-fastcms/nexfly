package com.nexfly.system.mapper;

import com.nexfly.system.model.DocumentSegment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocumentSegmentMapper {
    DocumentSegment findById(@Param("segmentId") Long segmentId);

    void save(@Param("documentSegment") DocumentSegment DocumentSegment);

    void update(@Param("documentSegment") DocumentSegment DocumentSegment);

    Long getCountByDatasetId(@Param("datasetId") Long datasetId);

    List<DocumentSegment> getListByDocumentId(@Param("documentId") Long documentId);

    void insertBatch(@Param("segmentList") List<DocumentSegment> segmentList);

    Long getCountByDocumentId(@Param("documentId") Long documentId);

    void deleteByDocumentIds(@Param("documentIds") List<Long> documentIds);

}
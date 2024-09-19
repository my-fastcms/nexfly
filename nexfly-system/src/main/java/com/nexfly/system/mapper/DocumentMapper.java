package com.nexfly.system.mapper;

import com.nexfly.system.model.Document;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocumentMapper {
    Document findById(@Param("documentId") Long documentId);

    void save(@Param("document") Document Document);

    void update(@Param("document") Document Document);

    List<Document> findDocumentListByDatasetId(@Param("datasetId") Long datasetId);

    Long getCountByDatasetId(@Param("datasetId") Long datasetId);

}
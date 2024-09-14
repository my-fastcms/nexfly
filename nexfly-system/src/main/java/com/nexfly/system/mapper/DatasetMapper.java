package com.nexfly.system.mapper;

import com.nexfly.system.model.Dataset;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DatasetMapper {
    Dataset findById(@Param("datasetId") Long datasetId);

    void save(@Param("dataset") Dataset Dataset);

    void update(@Param("dataset") Dataset Dataset);

    List<Dataset> findDatasetListByAppId(@Param("appId") Long appId);

}
package com.nexfly.system.mapper;

import com.nexfly.system.model.Dataset;
import com.nexfly.system.service.DatasetService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DatasetMapper {
    Dataset findById(@Param("datasetId") Long datasetId);

    void save(@Param("dataset") Dataset Dataset);

    void update(@Param("dataset") Dataset Dataset);

    List<Dataset> findDatasetListByAppId(@Param("appId") Long appId);

    List<Dataset> findDatasetList(@Param("userId") Long userId);

    void deleteAppDataset(@Param("appId") Long appId);

    void insertAppDatasetList(@Param("appDatasetList") List<DatasetService.AppDataset> appDatasetList);

    void deleteByDatasetId(@Param("datasetId") Long datasetId);

}
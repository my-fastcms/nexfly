package com.nexfly.system.service;

import com.nexfly.system.model.Dataset;

import java.util.Date;
import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/9/9
 **/
public interface DatasetService {

    DatasetDetailResponse getById(Long datasetId);

    void save(Dataset dataset);

    List<DatasetResponse> list(Long userId);

    void deleteByDatasetId(DeleteRequest deleteRequest);

    record DatasetResponse(Long datasetId, String name, String description, Long docNum, Long chunkNum, Date createDate, Date updateDate) {

    }

    record AppDataset(Long appId, Long datasetId) {

    }

    record DatasetDetailResponse(Long datasetId, Long orgId, String name, String description, String embedModelId, String vsIndexNodeId,
                         String avatar, String language, String parserId, String parserConfig, Long docNum) {

    }

    record DeleteRequest(Long datasetId) {

    }

}

package com.nexfly.system.service;

import com.nexfly.system.model.Dataset;

import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/9/9
 **/
public interface DatasetService {

    void save(Dataset dataset);

    List<DatasetResponse> list(Long userId);

    record DatasetResponse(Long datasetId, String name, String description, Long docNum, Long chunkNum) {

    }

}

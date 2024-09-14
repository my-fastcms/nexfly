package com.nexfly.system.service.impl;

import com.nexfly.system.mapper.DatasetMapper;
import com.nexfly.system.model.Dataset;
import com.nexfly.system.service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author wangjun
 * @Date 2024/9/9
 **/
@Service
public class DatasetServiceImpl implements DatasetService {

    @Autowired
    private DatasetMapper datasetMapper;

    @Override
    public void save(Dataset dataset) {
        if (dataset.getDatasetId() != null) {
            datasetMapper.update(dataset);
        } else {
            datasetMapper.save(dataset);
        }
    }

}

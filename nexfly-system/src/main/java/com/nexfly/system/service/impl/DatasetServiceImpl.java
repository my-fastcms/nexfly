package com.nexfly.system.service.impl;

import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.common.core.utils.UuidUtil;
import com.nexfly.system.mapper.AccountMapper;
import com.nexfly.system.mapper.DatasetMapper;
import com.nexfly.system.mapper.DocumentMapper;
import com.nexfly.system.mapper.DocumentSegmentMapper;
import com.nexfly.system.model.Dataset;
import com.nexfly.system.service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/9/9
 **/
@Service
public class DatasetServiceImpl implements DatasetService {

    @Autowired
    private DatasetMapper datasetMapper;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DocumentSegmentMapper documentSegmentMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Dataset getById(Long datasetId) {
        return datasetMapper.findById(datasetId);
    }

    @Override
    public void save(Dataset dataset) {
        if (dataset.getDatasetId() != null) {
            datasetMapper.update(dataset);
        } else {
            String className = "Class" + UuidUtil.getSimpleUuid();
            dataset.setVsIndexNodeId(className);
            dataset.setOrgId(accountMapper.getUserOrg(AuthUtils.getUserId()).getOrgId());
            datasetMapper.save(dataset);
        }
    }

    @Override
    public List<DatasetResponse> list(Long userId) {
        List<Dataset> datasetList = datasetMapper.findDatasetList(userId);
        List<DatasetResponse> datasetRespList = new ArrayList<>();

        for (Dataset dataset : datasetList) {
            Long docNum = documentMapper.getCountByDatasetId(dataset.getDatasetId());
            Long chunkNum = documentSegmentMapper.getCountByDatasetId(dataset.getDatasetId());
            datasetRespList.add(new DatasetResponse(dataset.getDatasetId(), dataset.getName(), dataset.getDescription(), docNum, chunkNum, dataset.getCreateAt(), dataset.getUpdateAt()));
        }
        
        return datasetRespList;
    }

}

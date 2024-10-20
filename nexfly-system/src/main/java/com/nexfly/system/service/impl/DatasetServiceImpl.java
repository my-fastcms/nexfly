package com.nexfly.system.service.impl;

import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.common.core.constants.NexflyConstants;
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
    public DatasetDetailResponse getById(Long datasetId) {
        Dataset dataset = datasetMapper.findById(datasetId);
        Long docNum = documentMapper.getCountByDatasetId(dataset.getDatasetId());
        return new DatasetDetailResponse(dataset.getDatasetId(), dataset.getOrgId(), dataset.getName(),
        dataset.getDescription(), dataset.getEmbedModelId(), dataset.getVsIndexNodeId(), dataset.getAvatar(), dataset.getLanguage(), dataset.getParserId(), dataset.getParserConfig(), docNum);
    }

    @Override
    public void save(Dataset dataset) {
        if (dataset.getDatasetId() != null) {
            datasetMapper.update(dataset);
        } else {
            String className = "Class" + UuidUtil.getSimpleUuid();
            dataset.setVsIndexNodeId(className);
            dataset.setOrgId(accountMapper.getUserOrg(AuthUtils.getUserId()).getOrgId());
            dataset.setStatus(NexflyConstants.Status.NORMAL.getValue());
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

    @Override
    public void deleteByDatasetId(DeleteRequest deleteRequest) {
        Long count = documentMapper.getCountByDatasetId(deleteRequest.datasetId());
        if (count == 0) {
            datasetMapper.deleteByDatasetId(deleteRequest.datasetId());
        } else {
            Dataset dataset = datasetMapper.findById(deleteRequest.datasetId());
            if (dataset != null) {
                dataset.setStatus(0);
                datasetMapper.update(dataset);
            }
        }
    }

}

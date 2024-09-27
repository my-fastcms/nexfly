package com.nexfly.system.service.impl;

import com.nexfly.common.core.exception.NexflyException;
import com.nexfly.system.mapper.DocumentMapper;
import com.nexfly.system.model.Document;
import com.nexfly.system.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/8/25
 **/
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentMapper documentMapper;

    @Override
    public List<Document> list(Long datasetId) {
        return documentMapper.findDocumentListByDatasetId(datasetId);
    }

    @Override
    public void saveDocument(DocumentParam documentParam) throws NexflyException {
        Document document = new Document();
        document.setName(documentParam.name());
        document.setOrgId(documentParam.orgId());
        document.setDatasetId(documentParam.datasetId());
        documentMapper.save(document);
    }

}

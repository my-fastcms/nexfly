package com.nexfly.system.service.impl;

import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.common.core.exception.NexflyException;
import com.nexfly.oss.common.OssFileManager;
import com.nexfly.system.mapper.AttachmentMapper;
import com.nexfly.system.model.Attachment;
import com.nexfly.system.service.AttachmentService;
import com.nexfly.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * @author wangjun
 **/
@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private OssFileManager ossFileManager;

    @Autowired
    private SystemService systemService;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Override
    public Attachment upload(String fileName, Long fileSize, String fileContentType, InputStream inputStream) {
        String object;
        try {
            object = ossFileManager.upload(fileName, fileContentType, inputStream);
        } catch (NexflyException e) {
            throw new RuntimeException(e);
        }

        Long orgId = systemService.getOrgId(AuthUtils.getUserId());
        Attachment attachment = new Attachment();
        attachment.setOrgId(orgId);
        attachment.setFileName(fileName);
        attachment.setPath(object);
        attachment.setSize(fileSize);
        attachment.setType(Objects.requireNonNull(getFileNameSuffix(fileName)));
        attachmentMapper.save(attachment);
        return attachment;
    }

    @Override
    public List<Attachment> getAttachmentListByIds(ListByIdsRequest list) {
        return attachmentMapper.getAttachmentListByIds(list.documentIds());
    }

    @Override
    public void delete(DeleteRequest deleteRequest) {
        attachmentMapper.deleteByIds(deleteRequest.documentIds());
    }

    private String getFileNameSuffix(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

}

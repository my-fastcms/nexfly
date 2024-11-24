package com.nexfly.system.service;

import com.nexfly.system.model.Attachment;

import java.io.InputStream;
import java.util.List;

/**
 * @author wangjun
 **/
public interface AttachmentService {

    Attachment upload(String fileName, Long fileSize, String fileContentType, InputStream inputStream);

    List<Attachment> getAttachmentListByIds(ListByIdsRequest list);

    void delete(DeleteRequest deleteRequest);

    record ListByIdsRequest(List<Long> documentIds) {

    }

    record DeleteRequest(List<Long> documentIds) {

    }

}

package com.nexfly.system.mapper;

import com.nexfly.system.model.Attachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttachmentMapper {
    Attachment findById(@Param("fileId") Long fileId);

    void save(@Param("file") Attachment attachment);

    void update(@Param("file") Attachment attachment);

    List<Attachment> getAttachmentListByIds(@Param("attachmentIds") List<Long> attachmentIds);

    void deleteByIds(@Param("attachmentIds") List<Long> attachmentIds);

}
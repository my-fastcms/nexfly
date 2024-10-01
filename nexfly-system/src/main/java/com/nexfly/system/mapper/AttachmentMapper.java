package com.nexfly.system.mapper;

import com.nexfly.system.model.Attachment;
import org.apache.ibatis.annotations.Param;

public interface AttachmentMapper {
    Attachment findById(@Param("fileId") Long fileId);

    void save(@Param("file") Attachment attachment);

    void update(@Param("file") Attachment attachment);

}
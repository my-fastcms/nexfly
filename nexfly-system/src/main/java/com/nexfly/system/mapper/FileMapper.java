package com.nexfly.system.mapper;

import com.nexfly.system.model.File;
import org.apache.ibatis.annotations.Param;

public interface FileMapper {
    File findById(@Param("fileId") Long fileId);

    void save(@Param("file") File File);

    void update(@Param("file") File File);
}
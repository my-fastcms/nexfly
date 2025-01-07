package com.nexfly.system.mapper;

import com.nexfly.system.model.AccountOpenid;
import org.apache.ibatis.annotations.Param;

public interface AccountOpenidMapper {
    AccountOpenid findById(@Param("openidId") Long openidId);

    void save(@Param("accountOpenid") AccountOpenid AccountOpenid);

    void update(@Param("accountOpenid") AccountOpenid AccountOpenid);
}
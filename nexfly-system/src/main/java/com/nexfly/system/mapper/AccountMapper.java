package com.nexfly.system.mapper;

import com.nexfly.system.model.Account;
import com.nexfly.system.service.SystemService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountMapper {
    Account findById(@Param("accountId") Long accountId);

    List<SystemService.Org> getUserOrgList(@Param("accountId") Long accountId);

    SystemService.Org getUserOrg(@Param("accountId") Long accountId);

    void save(@Param("account") Account Account);

    void update(@Param("account") Account Account);

    Account getAccountByEmail(@Param("email") String email);

    Account getAccountByUsername(@Param("username") String username);

}
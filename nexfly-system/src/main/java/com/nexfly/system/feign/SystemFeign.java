package com.nexfly.system.feign;

import com.nexfly.api.system.bean.Oauth2UserInfo;
import com.nexfly.api.system.feign.SystemFeignClient;
import com.nexfly.common.core.rest.RestResult;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.system.model.Account;
import com.nexfly.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemFeign implements SystemFeignClient {

    @Autowired
    private SystemService systemService;

    @Override
    public RestResult<AccountInfo> getAccountByUsername(String username) {
        Account account = systemService.getAccountByUsername(username);
        return account == null ? RestResultUtils.success(null) :
                RestResultUtils.success(new AccountInfo(account.getAccountId(), account.getUsername(), account.getEmail(), account.getPassword(), account.getStatus()));
    }

    @Override
    public RestResult<AccountInfo> getAccountByEmail(String email) {
        Account account = systemService.getAccountByEmail(email);
        return account == null ? RestResultUtils.success(null) :
                RestResultUtils.success(new AccountInfo(account.getAccountId(), account.getUsername(), account.getEmail(), account.getPassword(), account.getStatus()));
    }

    @Override
    public RestResult<AccountInfo> oauth2Login(Oauth2UserInfo oauth2UserInfo) {
        return RestResultUtils.success(systemService.oauth2Login(oauth2UserInfo));
    }

}

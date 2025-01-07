package com.nexfly.api.system.feign;

import com.nexfly.api.system.bean.Oauth2UserInfo;
import com.nexfly.common.core.feign.FeignInsideAuthConfig;
import com.nexfly.common.core.rest.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "nexfly-system", contextId = "system")
public interface SystemFeignClient {

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/system/getAccountByUsername")
    RestResult<AccountInfo> getAccountByUsername(@RequestParam("username") String username);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/system/getAccountByEmail")
    RestResult<AccountInfo> getAccountByEmail(@RequestParam("email") String email);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/system/oauth2Login")
    RestResult<AccountInfo> oauth2Login(@RequestBody Oauth2UserInfo oauth2UserInfo);

    record AccountInfo(Long accountId, String username, String email, String password, Integer status) {
    }

}

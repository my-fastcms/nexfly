package com.nexfly.api.system.feign;

import com.nexfly.common.core.feign.FeignInsideAuthConfig;
import com.nexfly.common.core.rest.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "nexfly-system", contextId = "system")
public interface SystemFeignClient {

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/system/getAccountByEmail")
    RestResult<AccountInfo> getAccountByEmail(@RequestParam("email") String email);

    record AccountInfo(Long accountId, String email, String password, Integer status) {
    }

}

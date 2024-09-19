package com.nexfly.system.controller;

import com.nexfly.ai.common.provider.ProviderManager;
import com.nexfly.ai.common.provider.SystemProvider;
import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.rest.RestResult;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.system.model.ProviderModel;
import com.nexfly.system.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/8/24
 **/
@RestController
@RequestMapping(NexflyConstants.API_PREFIX_MAPPING + "/provider")
public class ProviderController {

    @Autowired
    private ProviderManager providerManager;

    @Autowired
    private ProviderService providerService;

    @GetMapping("system/list")
    public RestResult<List<SystemProvider>> list() {
        return RestResultUtils.success(providerManager.getSystemProvider());
    }

    @GetMapping("user/list")
    public RestResult<List<ProviderModel>> userProviderList(@RequestParam("orgId") Long orgId) {
        return RestResultUtils.success(providerService.getProviderModelListByOrgId(orgId));
    }

    @PostMapping("add")
    public RestResult<Boolean> add() {
        return RestResultUtils.success(true);
    }

}

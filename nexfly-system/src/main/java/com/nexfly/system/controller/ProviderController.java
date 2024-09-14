package com.nexfly.system.controller;

import com.nexfly.ai.common.provider.ProviderManager;
import com.nexfly.ai.common.provider.SystemProvider;
import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.rest.RestResult;
import com.nexfly.common.core.rest.RestResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("system/list")
    public RestResult<List<SystemProvider>> list() {
        return RestResultUtils.success(providerManager.getSystemProvider());
    }

}

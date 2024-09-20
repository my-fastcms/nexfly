package com.nexfly.system.controller;

import com.nexfly.ai.common.provider.ProviderManager;
import com.nexfly.ai.common.provider.SystemProvider;
import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.exception.NexflyException;
import com.nexfly.common.core.rest.RestResult;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.system.model.ProviderModel;
import com.nexfly.system.service.ProviderService;
import com.nexfly.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private SystemService systemService;

    @GetMapping("system/list")
    public RestResult<List<SystemProvider>> list() {
        return RestResultUtils.success(providerManager.getSystemProviderList());
    }

    @GetMapping("user/list")
    public RestResult<Map<String, Object>> userProviderList() {
        return RestResultUtils.success(providerService.getUserAvailableProviderList());
    }

    @PostMapping("add")
    public RestResult<Boolean> add() {
        return RestResultUtils.success(true);
    }

    @PostMapping("save/apikey")
    public RestResult<Boolean> saveApiKey(@RequestBody ProviderModel providerModel) throws NexflyException {
        providerService.saveProviderModel(providerModel);
        return RestResultUtils.success();
    }

}

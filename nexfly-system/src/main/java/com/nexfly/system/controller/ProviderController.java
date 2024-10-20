package com.nexfly.system.controller;

import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.rest.RestResult;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.system.manager.ModelManager;
import com.nexfly.system.service.ProviderService;
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
    private ProviderService providerService;

    @Autowired
    private ModelManager modelManager;

    @GetMapping("system/list")
    public RestResult<List<ProviderService.SystemProviderInfo>> list() {
        return RestResultUtils.success(providerService.getSystemProviderList());
    }

    @GetMapping("user/list")
    public RestResult<Map<String, Object>> userProviderList() {
        return RestResultUtils.success(providerService.getUserAddedProviderList());
    }

    @GetMapping("available/list")
    public RestResult<Map<String, Object>> availableProviderList() {
        return RestResultUtils.success(providerService.getUserAvailableProviderList());
    }

    @PostMapping("add")
    public RestResult<Boolean> saveApiKey(@RequestBody ProviderService.ApiKeyRequest apiKeyRequest) throws Exception {
        if (!modelManager.checkApiKey(apiKeyRequest)) {
            return RestResultUtils.failed("无效的apiKey");
        }
        providerService.saveApiKey(apiKeyRequest);
        return RestResultUtils.success();
    }

}

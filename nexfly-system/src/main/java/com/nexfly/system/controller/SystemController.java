package com.nexfly.system.controller;

import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.common.core.utils.VersionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author wangjun
 * @Date 2024/9/22
 **/
@RestController
@RequestMapping(NexflyConstants.API_PREFIX_MAPPING + "/system")
public class SystemController {

    @GetMapping("version")
    public Object getVersion() {
        return RestResultUtils.success(VersionUtils.version);
    }

    @GetMapping("status")
    public Object getStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("es" , "");
        result.put("minio" , "");
        result.put("mysql" , "");
        result.put("redis" , "");
        return RestResultUtils.success(result);
    }

}

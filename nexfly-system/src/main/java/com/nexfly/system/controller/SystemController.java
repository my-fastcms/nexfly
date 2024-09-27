package com.nexfly.system.controller;

import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.common.core.utils.VersionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

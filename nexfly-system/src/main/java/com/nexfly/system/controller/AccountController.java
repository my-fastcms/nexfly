package com.nexfly.system.controller;

import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.rest.RestResult;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.system.model.Account;
import com.nexfly.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(NexflyConstants.API_PREFIX_MAPPING + "/account")
public class AccountController {

    @Autowired
    private SystemService systemService;

    @GetMapping("get")
    public RestResult<Account> getAccount() {
        return RestResultUtils.success(systemService.findById(AuthUtils.getUserId()));
    }

    @GetMapping("org/list")
    public RestResult<List<SystemService.Org>> getUserOrgList() {
        return RestResultUtils.success(systemService.getUserOrgList(AuthUtils.getUserId()));
    }

}

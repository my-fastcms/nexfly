package com.nexfly.auth.service;

import com.nexfly.api.system.feign.SystemFeignClient;
import com.nexfly.common.auth.model.UserInfo;
import com.nexfly.common.core.rest.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
public class NexflyUserDetailsService implements UserDetailsService {

    @Autowired
    private SystemFeignClient systemFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RestResult<SystemFeignClient.AccountInfo> accountResp = systemFeignClient.getAccountByEmail(username);
        SystemFeignClient.AccountInfo data = Optional.ofNullable(accountResp.getData()).orElseThrow();
        return new UserInfo(data.accountId(), data.email(), data.password(), Collections.synchronizedCollection(new ArrayList<>()));
    }

}

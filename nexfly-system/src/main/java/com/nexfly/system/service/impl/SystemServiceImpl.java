package com.nexfly.system.service.impl;

import com.nexfly.api.system.bean.Oauth2UserInfo;
import com.nexfly.api.system.feign.SystemFeignClient;
import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.system.mapper.AccountMapper;
import com.nexfly.system.mapper.AccountOpenidMapper;
import com.nexfly.system.model.Account;
import com.nexfly.system.model.AccountOpenid;
import com.nexfly.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountOpenidMapper accountOpenidMapper;

    @Override
    public Account getAccountByUsername(String username) {
        return accountMapper.getAccountByUsername(username);
    }

    @Override
    public Account getAccountByEmail(String email) {
        return accountMapper.getAccountByEmail(email);
    }

    @Override
    public Account findById(Long userId) {
        return accountMapper.findById(userId);
    }

    @Override
    public List<Org> getUserOrgList(Long userId) {
        return accountMapper.getUserOrgList(userId);
    }

    @Override
    public Org getUserOrg(Long userId) {
        return accountMapper.getUserOrg(userId);
    }

    @Override
    public Long getOrgId(Long userId) {
        return getUserOrg(userId).getOrgId();
    }

    @Override
    public Boolean update(AccountUpdateRequest accountUpdateRequest) {
        Account account = findById(AuthUtils.getUserId());
        account.setAvatar(accountUpdateRequest.avatar());
        account.setLanguage(accountUpdateRequest.language());
        account.setNickname(accountUpdateRequest.nickname());
        account.setTimezone(accountUpdateRequest.timezone());
        account.setTheme(accountUpdateRequest.color_schema());
        accountMapper.update(account);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemFeignClient.AccountInfo oauth2Login(Oauth2UserInfo oauth2UserInfo) {
        Account account = getAccountByUsername(oauth2UserInfo.getUsername());
        if (account == null) {
            // 初次oauth2授权
            account = new Account();
            account.setUsername(oauth2UserInfo.getUsername());
            account.setNickname(oauth2UserInfo.getNickname());
            account.setStatus(NexflyConstants.Status.NORMAL.getValue());
            account.setCreateBy(0L);
            account.setUpdateBy(0L);
            accountMapper.save(account);

            // 记录openid
            AccountOpenid openid = new AccountOpenid();
            openid.setOpenid(oauth2UserInfo.getOpenid());
            openid.setUsername(oauth2UserInfo.getUsername());
            openid.setClientId(oauth2UserInfo.getClientId());
            accountOpenidMapper.save(openid);
        }
        return new SystemFeignClient.AccountInfo(account.getAccountId(), account.getUsername(), account.getEmail(), account.getPassword(), account.getStatus());
    }

}

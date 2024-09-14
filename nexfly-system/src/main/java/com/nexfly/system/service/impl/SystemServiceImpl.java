package com.nexfly.system.service.impl;

import com.nexfly.system.mapper.AccountMapper;
import com.nexfly.system.model.Account;
import com.nexfly.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private AccountMapper accountMapper;

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

}

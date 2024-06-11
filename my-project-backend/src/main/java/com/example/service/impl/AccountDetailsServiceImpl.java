package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.vo.request.DetailsSaveVO;
import com.example.mapper.AccountDetailsMapper;
import com.example.service.AccountDetailsService;
import com.example.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @program: forum
 * @description: 用户详细信息service实现类
 * @author: 王贝强
 * @create: 2024-06-08 16:44
 */
@Service
public class AccountDetailsServiceImpl extends ServiceImpl<AccountDetailsMapper, AccountDetails> implements AccountDetailsService{
    @Resource
    AccountService service;
    @Override
    public AccountDetails findAccountDetailsByAccountId(Integer accountId) {
        return this.getById(accountId);
    }

    @Override
    @Transactional
    public synchronized boolean saveAccountDetails(Integer id, DetailsSaveVO detailsSaveVO) {
        Account account=service.findAccountByNameOrEmail(detailsSaveVO.getUsername());
        if(account==null|| Objects.equals(account.getId(), id)){
            service.update()
                    .eq("id", id)
                    .set("username", detailsSaveVO.getUsername())
                    .update();
            this.saveOrUpdate(new AccountDetails(
                    id,
                    detailsSaveVO.getGender(),
                    detailsSaveVO.getPhone(),
                    detailsSaveVO.getQq(),
                    detailsSaveVO.getWx(),
                    detailsSaveVO.getIntro()));
            return true;
        }
        return false;
    }
}

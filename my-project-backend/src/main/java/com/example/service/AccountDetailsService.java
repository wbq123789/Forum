package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.AccountDetails;
import com.example.entity.vo.request.DetailsSaveVO;

/**
 * @program: forum
 * @description: 用户详细信息service接口类
 * @author: 王贝强
 * @create: 2024-06-08 16:44
 */
public interface AccountDetailsService extends IService<AccountDetails> {
    AccountDetails findAccountDetailsByAccountId(Integer accountId);
    boolean saveAccountDetails(Integer id, DetailsSaveVO detailsSaveVO);
}

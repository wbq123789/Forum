package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.request.PrivacySaveVO;

/**
 * @program: forum
 * @description: 用户隐私信息Service接口类
 * @author: 王贝强
 * @create: 2024-06-09 20:33
 */
public interface AccountPrivacyService extends IService<AccountPrivacy> {
    void savePrivacy(Integer userId, PrivacySaveVO privacySaveVO);
    AccountPrivacy getAccountPrivacy(Integer userId);
}

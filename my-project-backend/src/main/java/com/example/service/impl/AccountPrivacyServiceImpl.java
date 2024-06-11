package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.request.PrivacySaveVO;
import com.example.mapper.AccountPrivacyMapper;
import com.example.service.AccountPrivacyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @program: forum
 * @description: 用户隐私信息Service实现类
 * @author: 王贝强
 * @create: 2024-06-09 20:35
 */
@Service
public class AccountPrivacyServiceImpl extends ServiceImpl<AccountPrivacyMapper, AccountPrivacy> implements AccountPrivacyService {
    @Override
    @Transactional
    public void savePrivacy(Integer userId, PrivacySaveVO privacySaveVO) {
        AccountPrivacy privacy = Optional.ofNullable(this.getById(userId)).orElse(new AccountPrivacy(userId));
        boolean status = privacySaveVO.isStatus();
        switch (privacySaveVO.getType()) {
            case "phone"->privacy.setPhone(status);
            case "email"->privacy.setEmail(status);
            case "qq"->privacy.setQq(status);
            case "wx"->privacy.setWx(status);
            case "gender"->privacy.setGender(status);
        }
        this.saveOrUpdate(privacy);
    }

    @Override
    public AccountPrivacy getAccountPrivacy(Integer userId) {
        return Optional.ofNullable(this.getById(userId)).orElse(new AccountPrivacy(userId));
    }
}

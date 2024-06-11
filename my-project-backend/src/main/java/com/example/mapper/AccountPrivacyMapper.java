package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.AccountPrivacy;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: forum
 * @description: 隐私设置mapper接口类
 * @author: 王贝强
 * @create: 2024-06-09 20:31
 */
@Mapper
public interface AccountPrivacyMapper extends BaseMapper<AccountPrivacy> {
}

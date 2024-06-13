package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Account;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: forum
 * @description: 用户mapper接口类
 * @create: 2024-06-07 20:55
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}

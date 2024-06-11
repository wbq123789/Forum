package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.AccountDetails;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: forum
 * @description: 用户详细信息mapper接口类
 * @author: 王贝强
 * @create: 2024-06-08 16:42
 */
@Mapper
public interface AccountDetailsMapper extends BaseMapper<AccountDetails>{
}

package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.Data;

/**
 * @program: forum
 * @description: 隐私设置实体类
 * @author: 王贝强
 * @create: 2024-06-09 20:23
 */
@Data
@TableName("account_privacy")
public class AccountPrivacy implements BaseData{
    @TableId(type = IdType.AUTO)
    final Integer id;
    boolean phone=false;
    boolean email=false;
    boolean qq=false;
    boolean wx=false;
    boolean gender=false;
}

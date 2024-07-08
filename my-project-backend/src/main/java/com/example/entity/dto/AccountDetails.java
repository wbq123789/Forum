package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @program: forum
 * @description: 用户详细信息实体类
 * @author: 王贝强
 * @create: 2024-06-08 16:36
 */
@Data
@TableName("account_details")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetails implements BaseData {
    @TableId
    Integer id;
    Integer gender;
    String phone;
    String qq;
    String wx;
    String intro;
}

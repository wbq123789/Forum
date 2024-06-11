package com.example.entity.vo.response;

import lombok.Data;

/**
 * @program: forum
 * @description: 用户详细信息响应类
 * @author: 王贝强
 * @create: 2024-06-08 18:33
 */
@Data
public class AccountDetailsVO {
    Integer gender;
    String phone;
    String qq;
    String wx;
    String intro;
}

package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @program: forum
 * @description: 用户响应类
 * @author: 王贝强
 * @create: 2024-06-07 20:57
 */
@Data
public class AccountVO {
    int id;
    String username;
    String email;
    String role;
    String avatar;
    Date registerTime;
}

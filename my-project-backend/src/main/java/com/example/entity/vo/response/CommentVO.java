package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @program: forum
 * @description: 评论返回视图类
 * @author: 王贝强
 * @create: 2024-06-24 11:11
 */
@Data
public class CommentVO {
    int id;
    String content;
    Date time;
    String quote;
    User user;

    @Data
    public static class User{
       Integer id;
       String username;
       String avatar;
       Integer gender;
       String qq;
       String wx;
       String phone;
       String email;
    }
}

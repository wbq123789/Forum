package com.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @program: forum
 * @description: 帖子详情页返回数据类
 * @author: 王贝强
 * @create: 2024-06-15 21:37
 */
@Data
public class TopicDetailVO {
    Integer id;
    String title;
    String content;
    Integer type;
    Date time;
    User user;
    Interact interact;
    Long comments;

    @Data
    @AllArgsConstructor
    public static class Interact {
        Boolean like;
        Boolean collect;
    }

    @Data
    public static class User {
        Integer id;
        String username;
        String avatar;
        String intro;
        Integer gender;
        String qq;
        String wx;
        String phone;
        String email;
    }
}

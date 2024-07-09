package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @program: forum
 * @description: 通知提示返回类
 * @author: 王贝强
 * @create: 2024-07-09 14:31
 */
@Data
public class NotificationVO {
    Integer id;
    String title;
    String content;
    String type;
    String url;
    Date time;
}

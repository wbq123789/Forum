package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @program: forum
 * @description: 置顶帖子视图类
 * @author: 王贝强
 * @create: 2024-06-15 20:08
 */
@Data
public class TopicTopVO {
    int id;
    String title;
    Date time;
}

package com.example.entity.vo.response;

import lombok.Data;

/**
 * @program: forum
 * @description: 帖子类型视图对象类
 * @author: 王贝强
 * @create: 2024-06-12 15:31
 */
@Data
public class TopicTypeVO {
    int id;
    String name;
    String intro;
    String color;
}

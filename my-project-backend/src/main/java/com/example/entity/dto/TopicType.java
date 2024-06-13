package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: forum
 * @description: 帖子主题类型实体类
 * @author: 王贝强
 * @create: 2024-06-12 15:20
 */
@Data
@TableName("topic_type")
@AllArgsConstructor
public class TopicType implements BaseData {
    @TableId(type = IdType.AUTO)
    Integer id;
    String name;
    String intro;
    String color;
}

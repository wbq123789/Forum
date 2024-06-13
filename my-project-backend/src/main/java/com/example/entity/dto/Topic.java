package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @program: forum
 * @description: 论坛帖子实体类
 * @author: 王贝强
 * @create: 2024-06-12 15:45
 */
@Data
@TableName("topic")
public class Topic {
    @TableId(type = IdType.AUTO)
    Integer id;
    String title;
    String content;
    Integer type;
    Date time;
    Integer uid;

}

package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.Data;

import java.util.Date;

/**
 * @program: forum
 * @description: 通知提醒实体类
 * @author: 王贝强
 * @create: 2024-07-09 14:17
 */
@Data
@TableName(value = "notification")
public class Notification implements BaseData{
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer uid;
    String title;
    String content;
    String type;
    String url;
    Date time;
}

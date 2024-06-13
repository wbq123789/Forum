package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @program: forum
 * @description: 图片存储实体类
 * @author: 王贝强
 * @create: 2024-06-12 12:06
 */
@Data
@TableName("image_store")
@AllArgsConstructor
public class ImageStore {
    @TableId
    Integer uid;
    String name;
    Date time;

}

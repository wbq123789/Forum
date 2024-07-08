package com.example.entity.vo.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * @program: forum
 * @description:  添加评论VO类，用于接收前端传来的评论数据
 * @author: 王贝强
 * @create: 2024-06-23 19:25
 */
@Data
public class AddCommentVO {
    @Min(1)
    int tid;
    String content;
    @Min(-1)
    int quote;
}

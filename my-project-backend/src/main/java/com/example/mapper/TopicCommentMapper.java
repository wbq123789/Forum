package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.TopicComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: forum
 * @description: 评论mapper接口类
 * @author: 王贝强
 * @create: 2024-06-23 20:55
 */
@Mapper
public interface TopicCommentMapper extends BaseMapper<TopicComment> {
}

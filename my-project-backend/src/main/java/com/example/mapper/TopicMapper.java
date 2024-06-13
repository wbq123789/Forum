package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Topic;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: forum
 * @description: 论坛帖子mapper接口类
 * @author: 王贝强
 * @create: 2024-06-12 15:49
 */
@Mapper
public interface TopicMapper extends BaseMapper<Topic> {
}

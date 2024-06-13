package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.TopicType;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: forum
 * @description: 论坛帖子主题相关mapper接口类
 * @author: 王贝强
 * @create: 2024-06-12 15:26
 */
@Mapper
public interface TopicTypeMapper extends BaseMapper<TopicType> {
}

package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Notification;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: forum
 * @description: 通知提醒mapper接口
 * @author: 王贝强
 * @create: 2024-07-09 14:20
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}

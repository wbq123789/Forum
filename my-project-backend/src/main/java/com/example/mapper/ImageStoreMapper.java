package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.ImageStore;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: forum
 * @description: 图片存储mapper接口类
 * @author: 王贝强
 * @create: 2024-06-12 12:09
 */
@Mapper
public interface ImageStoreMapper extends BaseMapper<ImageStore> {
}

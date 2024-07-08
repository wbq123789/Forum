package com.example.entity.vo.request;

import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @program: forum
 * @description:
 * @author: 王贝强
 * @create: 2024-06-12 17:11
 */
@Data
public class TopicCreateVO {
    @Min(1)
    int type;
    @Length(min = 1, max = 30)
    String title;
    JSONObject content;
}

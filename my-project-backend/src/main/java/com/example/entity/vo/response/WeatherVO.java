package com.example.entity.vo.response;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @program: forum
 * @description:
 * @author: 王贝强
 * @create: 2024-06-11 12:12
 */
@Data
public class WeatherVO {
    JSONObject location;
    JSONObject now;
    JSONArray hourly;
}

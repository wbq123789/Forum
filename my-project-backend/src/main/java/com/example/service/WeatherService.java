package com.example.service;

import com.example.entity.vo.response.WeatherVO;

/**
 * @program: forum
 * @description: 天气相关服务接口
 * @author: 王贝强
 * @create: 2024-06-11 12:17
 */
public interface WeatherService {
    WeatherVO fetchWeather(double longitude, double latitude);
}

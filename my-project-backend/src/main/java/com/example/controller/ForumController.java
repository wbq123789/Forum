package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.WeatherVO;
import com.example.service.WeatherService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: forum
 * @description: 论坛相关接口
 * @author: 王贝强
 * @create: 2024-06-11 12:09
 */
@RestController
@RequestMapping("/api/forum")
public class ForumController {
    @Resource
    WeatherService weatherService;

    @GetMapping("/weather")
    public RestBean<WeatherVO> getWeather(double longitude, double latitude){
        WeatherVO vo = weatherService.fetchWeather(longitude, latitude);
        return vo==null ? RestBean.failure(400,"获取地理位置与天气失败，请联系管理员"):RestBean.success(vo);
    }
    @GetMapping("/getIP")
    public RestBean<String> getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return RestBean.success(ipAddress);
    }
}

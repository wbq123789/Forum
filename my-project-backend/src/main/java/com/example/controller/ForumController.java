package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.*;
import com.example.service.TopicService;
import com.example.service.WeatherService;
import com.example.utils.Const;
import com.example.utils.ControllerUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Resource
    TopicService topicService;
    @Resource
    ControllerUtils utils;

    @GetMapping("/weather")
    public RestBean<WeatherVO> getWeather(double longitude, double latitude){
        WeatherVO vo = weatherService.fetchWeather(longitude, latitude);
        return vo==null ? RestBean.failure(400,"获取地理位置与天气失败，请联系管理员"):RestBean.success(vo);
    }
    @GetMapping("/types")
    public RestBean<List<TopicTypeVO>> getTypes(){
        return RestBean.success(topicService.listTypes().stream().map(topicType -> topicType.asViewObject(TopicTypeVO.class)).toList());
    }
    @PostMapping("/create-topic")
    public RestBean<Void> createTopic(@Valid @RequestBody TopicCreateVO vo,
                                      @RequestAttribute(Const.ATTR_USER_ID) int uid){
        return utils.messageHandle(() -> topicService.createTopic(uid, vo));
    }
    @GetMapping("/list-topic")
    public RestBean<List<TopicPreviewVO>> listTopic(@RequestParam @Min(0) int page,
                                                    @RequestParam @Min(0) int type){
        return RestBean.success(topicService.listTopicByPage(page, type));
    }
    @GetMapping("/top-topic")
    public RestBean<List<TopicTopVO>> topTopic(){
        return RestBean.success(topicService.listTopTopics());
    }
    @GetMapping("/topic")
    public RestBean<TopicDetailVO> getTopic(@RequestParam @Min(1) int tid){
        return RestBean.success(topicService.getTopic(tid));
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

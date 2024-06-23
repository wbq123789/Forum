package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Interact;
import com.example.entity.vo.response.*;
import com.example.service.TopicService;
import com.example.service.WeatherService;
import com.example.utils.Const;
import com.example.utils.ControllerUtils;
import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @program: forum
 * @description: 论坛相关接口
 * @author: 王贝强
 * @create: 2024-06-11 12:09
 */
@RestController
@RequestMapping("/api/forum")
@Tag(name = "论坛相关", description = "包括天气查询、话题发布、话题列表等操作。")
public class ForumController {
    @Resource
    WeatherService weatherService;
    @Resource
    TopicService topicService;
    @Resource
    ControllerUtils utils;

    /**
     * 获取天气
     * @param longitude 经度
     * @param latitude 纬度
     * @return 天气信息
     */
    @GetMapping("/weather")
    @Operation(summary = "获取天气")
    public RestBean<WeatherVO> getWeather(double longitude, double latitude){
        WeatherVO vo = weatherService.fetchWeather(longitude, latitude);
        return vo==null ? RestBean.failure(400,"获取地理位置与天气失败，请联系管理员"):RestBean.success(vo);
    }
    /**
     * 获取话题类型
     * @return 话题类型列表
     */
    @GetMapping("/types")
    @Operation(summary = "获取话题类型")
    public RestBean<List<TopicTypeVO>> getTypes(){
        return RestBean.success(topicService.listTypes().stream().map(topicType -> topicType.asViewObject(TopicTypeVO.class)).toList());
    }
    /**
     * 创建话题
     * @param vo 话题信息
     * @param uid 用户ID
     * @return 创建结果
     */
    @PostMapping("/create-topic")
    @Operation(summary = "创建话题")
    public RestBean<Void> createTopic(@Valid @RequestBody TopicCreateVO vo,
                                      @RequestAttribute(Const.ATTR_USER_ID) int uid){
        return utils.messageHandle(() -> topicService.createTopic(uid, vo));
    }
    /**
     * 获取话题列表
     * @param page 页码
     * @param type 话题类型
     * @return 话题列表
     */
    @GetMapping("/list-topic")
    @Operation(summary = "获取话题列表")
    public RestBean<List<TopicPreviewVO>> listTopic(@RequestParam @Min(0) int page,
                                                    @RequestParam @Min(0) int type){
        return RestBean.success(topicService.listTopicByPage(page, type));
    }
    /**
     * 获取置顶话题
     * @return 置顶话题列表
     */
    @GetMapping("/top-topic")
    @Operation(summary = "获取置顶话题")
    public RestBean<List<TopicTopVO>> topTopic(){
        return RestBean.success(topicService.listTopTopics());
    }
    /**
     * 获取话题详情
     * @param tid 话题ID
     * @return 话题详情
     */
    @GetMapping("/topic")
    @Operation(summary = "获取话题详情")
    public RestBean<TopicDetailVO> getTopic(@RequestParam @Min(1) int tid,
                                            @RequestAttribute(Const.ATTR_USER_ID) int uid){
        return RestBean.success(topicService.getTopic(tid,uid));
    }
    @GetMapping("/interact")
    public RestBean<Void> interact(@RequestParam @Min(1) int tid,
                                   @RequestParam @Pattern(regexp = ("like|collect")) String type,
                                   @RequestParam boolean state,
                                   @RequestAttribute(Const.ATTR_USER_ID) int uid){
        topicService.interact(new Interact(tid,uid,new Date(),type),state);
        return RestBean.success();
    }
    /**
     * 获取用户IP地址
     * @param request 请求
     * @return IP地址
     */
    @GetMapping("/getIP")
    @Operation(summary = "获取用户IP地址")
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

package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.NotificationVO;
import com.example.service.NotificationService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: forum
 * @description: 通知相关接口
 * @author: 王贝强
 * @create: 2024-07-09 15:04
 */
@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Resource
    NotificationService notificationService;

    @GetMapping("/list")
    public RestBean<List<NotificationVO>> listNotification(@RequestAttribute(Const.ATTR_USER_ID) int uid){
        return RestBean.success(notificationService.findUserNotification(uid));
    }
    @GetMapping("delete")
    public RestBean<Void> deleteNotificationById(@RequestAttribute(Const.ATTR_USER_ID) int uid,
                                             @RequestParam @Min(1) int id){
        notificationService.deleteUserNotification(id,uid);
        return RestBean.success();
    }
    @GetMapping("delete-all")
    public RestBean<Void> deleteAllUserNotification(@RequestAttribute(Const.ATTR_USER_ID) int uid){
        notificationService.deleteUserAllNotification(uid);
        return RestBean.success();
    }
}

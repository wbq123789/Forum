package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Notification;
import com.example.entity.vo.response.NotificationVO;
import com.example.mapper.NotificationMapper;
import com.example.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: forum
 * @description: 通知提醒服务接口实现类
 * @author: 王贝强
 * @create: 2024-07-09 14:24
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {
    @Override
    public List<NotificationVO> findUserNotification(int uid) {
        return this.list(Wrappers.<Notification>query().eq("uid", uid))
                .stream()
                .map(notification -> notification.asViewObject(NotificationVO.class))
                .toList();
    }

    public void deleteUserNotification(int id, int uid){
        this.remove(Wrappers.<Notification>query().eq("id", id).eq("uid", uid));
    }

    public void deleteUserAllNotification(int uid){
        this.remove(Wrappers.<Notification>query().eq("uid", uid));
    }

    @Override
    public void addNotification(int uid, String title, String content, String type, String url) {
        Notification notification = new Notification();
        notification.setUid(uid);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setTime(new Date());
        notification.setUrl(url);
        this.save(notification);
    }
}

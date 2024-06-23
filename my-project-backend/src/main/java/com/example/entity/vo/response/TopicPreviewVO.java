package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: forum
 * @description: 话题预览视图对象，用于展示话题列表时的帖子预览
 * @author: 王贝强
 * @create: 2024-06-12 23:08
 */
@Data
public class TopicPreviewVO {
    int id;
    int type;
    String title;
    String text;
    List<String> images;
    Date time;
    Integer uid;
    String username;
    String avatar;
    int like;
    int collect;
}

package com.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.*;
import com.example.entity.vo.response.TopicCreateVO;
import com.example.entity.vo.response.TopicDetailVO;
import com.example.entity.vo.response.TopicPreviewVO;
import com.example.entity.vo.response.TopicTopVO;
import com.example.mapper.*;
import com.example.service.TopicService;
import com.example.utils.CacheUtils;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: forum
 * @description: 论坛帖子主题相关服务层接口实现类
 * @author: 王贝强
 * @create: 2024-06-12 15:23
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {
    @Resource
    TopicTypeMapper topicTypeMapper;
    @Resource
    AccountMapper accountMapper;
    @Resource
    AccountDetailsMapper accountDetailsMapper;
    @Resource
    AccountPrivacyMapper accountPrivacyMapper;

    @Resource
    FlowUtils flowUtils;

    @Resource
    CacheUtils cacheUtils;

    private Set<Integer> types=null;

    @PostConstruct
    public void init(){
        types=this.listTypes()
                .stream()
                .map(TopicType::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public List<TopicType> listTypes() {
        return topicTypeMapper.selectList(null);
    }

    @Override
    public String createTopic(int uid, TopicCreateVO vo) {
        if(!textLimitCheck(vo.getContent()))
            return "帖子内容过长，发布失败！";
        if(!types.contains(vo.getType()))
            return "帖子类型非法！";
        String key= Const.FORUM_TOPIC_CREATE_COUNTER+uid;
        if(!flowUtils.limitPeriodCountCheck(key, 3, 3600))
            return "您的发帖速度过快，请稍后再试！";
        Topic topic=new Topic();
        BeanUtils.copyProperties(vo,topic);
        topic.setUid(uid);
        topic.setTime(new Date());
        topic.setContent(vo.getContent().toJSONString());
        if (this.save(topic)) {
            cacheUtils.deleteCachePattern(Const.FORUM_TOPIC_PREVIEW_CACHE+"*");
            return null;
        }
        return "内部错误，请联系管理员！";
    }

    @Override
    public List<TopicTopVO> listTopTopics() {
        List<Topic> topics=baseMapper.selectList(Wrappers.<Topic>query()
                .select("id","title","time")
                .eq("top",1));
        return topics.stream().map(topic -> {
            TopicTopVO vo=new TopicTopVO();
            BeanUtils.copyProperties(topic,vo);
            return vo;
        }).toList();
    }
    private <T> T fillUserDetailsByPrivacy(T target,int uid){
        AccountDetails details=accountDetailsMapper.selectById(uid);
        Account account=accountMapper.selectById(uid);
        AccountPrivacy privacy=Optional.ofNullable(accountPrivacyMapper.selectById(uid)).orElse(new AccountPrivacy(uid));
        String[] ignores = privacy.hiddenFields();
        BeanUtils.copyProperties(account,target,ignores);
        BeanUtils.copyProperties(details,target,ignores);
        return target;
    }

    @Override
    public List<TopicPreviewVO> listTopicByPage(int pageNumber, int type) {
        String key = Const.FORUM_TOPIC_PREVIEW_CACHE + pageNumber + ":" + type;
        List<TopicPreviewVO> list = cacheUtils.takeListFormCache(key, TopicPreviewVO.class);
        if(list != null)
            return list;
        Page<Topic> page = Page.of(pageNumber, 10);
        if(type == 0)
            baseMapper.selectPage(page,Wrappers.<Topic>query().orderByDesc("time"));
        else
            baseMapper.selectPage(page,Wrappers.<Topic>query().eq("type", type).orderByDesc("time"));
        List<Topic> topics = page.getRecords();
        if(topics.isEmpty()) return null;
        list = topics.stream().map(this::resolvePreview).toList();
        cacheUtils.saveListToCache(key, list, 60);
        return list;
    }

    @Override
    public TopicDetailVO getTopic(int tid) {
        TopicDetailVO vo=new TopicDetailVO();
        Topic topic=baseMapper.selectById(tid);
        BeanUtils.copyProperties(topic,vo);
        TopicDetailVO.User user=new TopicDetailVO.User();
        vo.setUser(fillUserDetailsByPrivacy(user,topic.getUid()));
        return vo;
    }

    private TopicPreviewVO resolvePreview(Topic topic){
        TopicPreviewVO vo=new TopicPreviewVO();
        BeanUtils.copyProperties(topic,vo);
        BeanUtils.copyProperties(accountMapper.selectById(topic.getUid()),vo,"uid","id");
        List<String> images=new ArrayList<>();
        StringBuilder previewText=new StringBuilder();
        JSONArray ops=JSONObject.parseObject(topic.getContent()).getJSONArray("ops");
        for (Object op : ops ) {
            Object insert=JSONObject.from(op).get("insert");
            if(insert instanceof String text) {
                if(previewText.length()>=300) continue;
                previewText.append(text);
            }else if(insert instanceof Map<?, ?> map) {
                Optional.ofNullable(map.get("image"))
                        .ifPresent(obj->images.add(obj.toString()));
            }
        }
        vo.setText(previewText.length()>300?previewText.substring(0,300):previewText.toString());
        vo.setImages(images);
        return vo;
    }

    private boolean textLimitCheck(JSONObject object){
        if(object==null) return false;
        long length = 0;
        for (Object op : object.getJSONArray("ops")) {
            length += JSONObject.from(op).getString("insert").length();
            if (length > 20000) return false;
        }
        return true;
    }
}

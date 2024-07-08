package com.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.*;
import com.example.entity.vo.request.AddCommentVO;
import com.example.entity.vo.request.TopicCreateVO;
import com.example.entity.vo.request.TopicUpdateVO;
import com.example.entity.vo.response.*;
import com.example.mapper.*;
import com.example.service.TopicService;
import com.example.utils.CacheUtils;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
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
    TopicCommentMapper topicCommentMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;
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
        if(!textLimitCheck(vo.getContent(),20000))
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
    public String updateTopic(int uid, TopicUpdateVO vo) {
        if(!textLimitCheck(vo.getContent(),20000))
            return "帖子内容过长，发布失败！";
        if(!types.contains(vo.getType()))
            return "帖子类型非法！";
        baseMapper.update(null,Wrappers.<Topic>update()
                .eq("uid",uid)
                .eq("id",vo.getId())
                .set("title",vo.getTitle())
                .set("type",vo.getType())
                .set("content",vo.getContent().toString()));
        return null;
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
    public List<TopicPreviewVO> listTopicCollects(int uid){
        return baseMapper.collectTopics(uid)
                .stream()
                .map(topic -> {
                    TopicPreviewVO vo=new TopicPreviewVO();
                    BeanUtils.copyProperties(topic,vo);
                    return vo;
                }).toList();
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
    public TopicDetailVO getTopic(int tid,int uid) {
        TopicDetailVO vo=new TopicDetailVO();
        Topic topic=baseMapper.selectById(tid);
        BeanUtils.copyProperties(topic,vo);
        TopicDetailVO.Interact interact=new TopicDetailVO.Interact(
                this.hasInteract(tid,uid,"like"),
                this.hasInteract(tid,uid,"collect")
        );
        vo.setInteract(interact);
        TopicDetailVO.User user=new TopicDetailVO.User();
        vo.setUser(fillUserDetailsByPrivacy(user,topic.getUid()));
        vo.setComments(topicCommentMapper.selectCount(Wrappers.<TopicComment>query().eq("tid",tid)));
        return vo;
    }

    @Override
    public void interact(Interact interact, boolean state) {
        String type=interact.getType();
        synchronized (type.intern()){
            stringRedisTemplate.opsForHash().put(type,interact.toKey(),Boolean.toString(state));
            this.saveInteractSchedule(type);
        }
    }

    @Override
    public String createComment(int uid, AddCommentVO vo) {
        String key= Const.FORUM_TOPIC_COMMENT_COUNTER+uid;
        if(!flowUtils.limitPeriodCountCheck(key, 2, 60))
            return "您发表评论过于频繁，请稍后再试！";
        if(!textLimitCheck(JSONObject.parseObject(vo.getContent()),2000))
            return "评论内容过长，发布失败！";
        TopicComment comment=new TopicComment();
        comment.setTime(new Date());
        comment.setUid(uid);
        BeanUtils.copyProperties(vo,comment);
        topicCommentMapper.insert(comment);
        return null;
    }

    @Override
    public List<CommentVO> comments(int tid, int page) {
        Page<TopicComment> pages=Page.of(page,10);
        topicCommentMapper.selectPage(pages,Wrappers.<TopicComment>query().eq("tid",tid));
        return pages.getRecords().stream().map(dto->{
            CommentVO vo=new CommentVO();
            BeanUtils.copyProperties(dto,vo);
            if(dto.getQuote()>0){
                TopicComment comment = topicCommentMapper.selectOne(Wrappers.<TopicComment>query().eq("id", dto.getQuote()).orderByAsc("time"));
                if (comment!=null){
                    JSONObject object=JSONObject.parseObject(comment.getContent());
                    StringBuilder builder=new StringBuilder();
                    shoutContent(object.getJSONArray("ops"),builder,ignore->{});
                    vo.setQuote(builder.toString());
                }else {
                    vo.setQuote("此评论已被删除");
                }

            }
            CommentVO.User user=new CommentVO.User();
            this.fillUserDetailsByPrivacy(user,dto.getUid());
            vo.setUser(user);
            return vo;
        }).toList();
    }

    @Override
    public String deleteComment(int cid, int uid) {
        int delete = topicCommentMapper.delete(Wrappers.<TopicComment>query().eq("id", cid).eq("uid", uid));
        return delete==1 ? null : "评论删除失败！";
    }

    private boolean hasInteract(int tid, int uid, String type){
        String key=tid+":"+uid;
        if (stringRedisTemplate.opsForHash().hasKey(type,key))
            return Boolean.parseBoolean(stringRedisTemplate.opsForHash().entries(type).get(key).toString());
        return baseMapper.userInteractCount(tid,uid,type)>0;
    }

    private final Map<String,Boolean> state=new HashMap<>();
    ScheduledExecutorService service= Executors.newScheduledThreadPool(2);

    private void saveInteractSchedule(String type){
        if(!state.getOrDefault(type,false)){
            state.put(type,true);
            service.schedule(()->{
                this.saveInteract(type);
                state.put(type,false);
            },3, TimeUnit.SECONDS);
        }
    }
    private void saveInteract(String type) {
        synchronized (type.intern()) {
            List<Interact> check = new LinkedList<>();
            List<Interact> uncheck = new LinkedList<>();
            stringRedisTemplate.opsForHash().entries(type).forEach((k, v) -> {
                if(Boolean.parseBoolean(v.toString()))
                    check.add(Interact.parseInteract(k.toString(), type));
                else
                    uncheck.add(Interact.parseInteract(k.toString(), type));
            });
            if(!check.isEmpty())
                baseMapper.addInteract(check, type);
            if(!uncheck.isEmpty())
                baseMapper.deleteInteract(uncheck, type);
            stringRedisTemplate.delete(type);
        }
    }

    private TopicPreviewVO resolvePreview(Topic topic){
        TopicPreviewVO vo=new TopicPreviewVO();
        BeanUtils.copyProperties(topic,vo);
        BeanUtils.copyProperties(accountMapper.selectById(topic.getUid()),vo,"uid","id");
        vo.setLike(baseMapper.interactCount(topic.getId(),"like"));
        vo.setCollect(baseMapper.interactCount(topic.getId(),"collect"));
        List<String> images=new ArrayList<>();
        StringBuilder previewText=new StringBuilder();
        JSONArray ops=JSONObject.parseObject(topic.getContent()).getJSONArray("ops");
        shoutContent(ops,previewText,obj->images.add(obj.toString()));
        vo.setText(previewText.length()>300?previewText.substring(0,300):previewText.toString());
        vo.setImages(images);
        return vo;
    }

    private void shoutContent(JSONArray ops, StringBuilder previewText, Consumer<Object> imageHandler){
        for (Object op : ops ) {
            Object insert=JSONObject.from(op).get("insert");
            if(insert instanceof String text) {
                if(previewText.length()>=300) continue;
                previewText.append(text);
            }else if(insert instanceof Map<?, ?> map) {
                Optional.ofNullable(map.get("image"))
                        .ifPresent(imageHandler);
            }
        }
    }
    private boolean textLimitCheck(JSONObject object,int max){
        if(object==null) return false;
        long length = 0;
        for (Object op : object.getJSONArray("ops")) {
            length += JSONObject.from(op).getString("insert").length();
            if (length > max) return false;
        }
        return true;
    }
}

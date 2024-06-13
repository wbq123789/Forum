package com.example.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @program: forum
 * @description: 缓存工具类，用于缓存一些数据，减少数据库访问次数，提高性能
 * @author: 王贝强
 * @create: 2024-06-13 15:37
 */
@Component
public class CacheUtils {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    public <T> T takeFormCache(String key,Class<T> dataType){
        String s=stringRedisTemplate.opsForValue().get(key);
        if(s==null) return null;
        return JSONArray.parseArray(s).to(dataType);
    }

    public <T> List<T> takeLisFormCache(String key,Class<T> itemType){
        String s=stringRedisTemplate.opsForValue().get(key);
        if(s==null) return null;
        return JSONArray.parseArray(s).toList(itemType);
    }

    public <T> void saveObject(String key, T list,long expire){
        stringRedisTemplate.opsForValue().set(key, JSONObject.from(list).toJSONString(),expire, TimeUnit.SECONDS);
    }

    public <T> void saveListObject(String key, List<T> list,long expire){
        stringRedisTemplate.opsForValue().set(key,JSONArray.from(list).toJSONString(),expire, TimeUnit.SECONDS);
    }
    public void deleteCachePattern(String key){
        Set<String> keys = Optional.ofNullable(stringRedisTemplate.keys(key)).orElse(Collections.emptySet());
        stringRedisTemplate.delete(keys);
    }
    public void deleteCache(String key){
        stringRedisTemplate.delete(key);
    }
}

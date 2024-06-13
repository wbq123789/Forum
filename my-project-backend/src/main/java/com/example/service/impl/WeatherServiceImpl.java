package com.example.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.entity.vo.response.WeatherVO;
import com.example.service.WeatherService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

/**
 * @program: forum
 * @description: 天气相关服务实现类
 * @author: 王贝强
 * @create: 2024-06-11 12:18
 */
@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {
    @Resource
    RestTemplate rest;
    @Value("${weather.key}")
    String weatherKey;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public WeatherVO fetchWeather(double longitude, double latitude) {

        return this.fetchFromCache(longitude, latitude);
    }

    private WeatherVO fetchFromCache(double longitude, double latitude) {
        byte[] data = rest.getForObject(
                "https://geoapi.qweather.com/v2/city/lookup?location=" + longitude+ "," + latitude+ "&key=" + weatherKey, byte[].class);
        JSONObject geo = decompressStringToJSON(data);
        if (geo == null)
            return null;
        JSONObject location =geo.getJSONArray("location").getJSONObject(0);
        int locationId = location.getIntValue("id");
        String redisKey = Const.FORUM_WEATHER_CACHE + locationId;
        String cache = stringRedisTemplate.opsForValue().get(redisKey);
        if (cache != null) {
            return JSONObject.parseObject(cache, WeatherVO.class);
        }
        WeatherVO vo = this.fetchFromAPI(locationId, location);
        if (vo == null) return null;
        stringRedisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(vo), 1, TimeUnit.HOURS);
        return vo;
    }

    private WeatherVO fetchFromAPI(int locationId, JSONObject location) {
        WeatherVO vo = new WeatherVO();
        vo.setLocation(location);
        byte[] data_now = rest.getForObject("https://devapi.qweather.com/v7/weather/now?location=" + locationId + "&key=" + weatherKey, byte[].class);
        JSONObject now = this.decompressStringToJSON(data_now);
        if (now == null) return null;
        vo.setNow(now.getJSONObject("now"));
        byte[] data_hour = rest.getForObject("https://devapi.qweather.com/v7/weather/24h?location=" + locationId + "&key=" + weatherKey, byte[].class);
        JSONObject hour = this.decompressStringToJSON(data_hour);
        if (hour == null) return null;
        vo.setHourly(new JSONArray(hour.getJSONArray("hourly").stream().limit(5).toList()));
        return vo;
    }

    private JSONObject decompressStringToJSON(byte[] data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(data));
            byte[] buffer = new byte[256];
            int read;
            while ((read = gzip.read(buffer)) >= 0) {
                out.write(buffer, 0, read);
            }
            return JSONObject.parseObject(out.toString());
        } catch (IOException e) {
            log.error("解压缩数据失败:{}", e.getMessage(), e);
            return null;
        }
    }
}

package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: forum
 * @description: 隐私设置实体类
 * @author: 王贝强
 * @create: 2024-06-09 20:23
 */
@Data
@Slf4j
@TableName("account_privacy")
public class AccountPrivacy implements BaseData{
    @TableId(type = IdType.AUTO)
    final Integer id;
    boolean phone=false;
    boolean email=false;
    boolean qq=false;
    boolean wx=false;
    boolean gender=false;

    public String[] hiddenFields(){
        List<String> strings=new LinkedList<>();
        Field[] fields=this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if(field.getType().equals(boolean.class) && !field.getBoolean(this))
                    strings.add(field.getName());
                }catch (Exception e){
                log.warn("个人隐私信息获取失败：{}",e.getMessage(),e);
            }
        }
        return strings.toArray(String[]::new);
    }
}

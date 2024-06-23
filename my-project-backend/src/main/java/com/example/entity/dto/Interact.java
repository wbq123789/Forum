package com.example.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @program: forum
 * @description: 交互实体类
 * @author: 王贝强
 * @create: 2024-06-20 12:50
 */
@Data
@AllArgsConstructor
public class Interact {
    Integer tid;
    Integer uid;
    Date time;
    String type;

    public String toKey(){
        return tid+":"+uid;
    }

    public static Interact parseInteract(String str,String type){
        String[] keys=str.split(":");
        return new Interact(Integer.parseInt(keys[0]),Integer.parseInt(keys[1]),new Date(),type);
    }
}

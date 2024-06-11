package com.example.entity.vo.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

/**
 * @program: forum
 * @description: 隐私设置保存VO类
 * @author: 王贝强
 * @create: 2024-06-09 20:05
 */
@Data
public class PrivacySaveVO {
    @Pattern(regexp = "phone|email|qq|wx|gender")
    String type;
    boolean status;
}

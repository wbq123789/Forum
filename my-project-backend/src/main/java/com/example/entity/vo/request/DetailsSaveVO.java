package com.example.entity.vo.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @program: forum
 * @description: 用户详细信息保存VO类
 * @author: 王贝强
 * @create: 2024-06-08 16:50
 */
@Data
public class DetailsSaveVO {
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 10)
    String username;
    @Min(0)@Max(1)
    Integer gender;
    @Pattern(regexp = "^(1[3-9]\\d{9})$|^(0\\d{2,3}-?\\d{7,8}(-\\d{1,4})?)$")
    @Length(max = 11)
    String phone;
    @Length(max = 13)
    String qq;
    String wx;
    @Length(max = 200)
    String intro;

}

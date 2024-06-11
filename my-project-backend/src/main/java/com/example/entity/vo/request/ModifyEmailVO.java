package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @program: forum
 * @description: 修改邮箱请求类
 * @author: 王贝强
 * @create: 2024-06-09 00:27
 */
@Data
public class ModifyEmailVO {
    @Email
    String email;
    @Length(min = 6, max = 6)
    String code;
}

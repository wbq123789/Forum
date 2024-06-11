package com.example.entity.vo.response;

import lombok.Data;

/**
 * @program: forum
 * @description: 隐私设置响应类
 * @author: 王贝强
 * @create: 2024-06-09 20:51
 */
@Data
public class AccountPrivacyVO {
    boolean phone;
    boolean email;
    boolean qq;
    boolean wx;
    boolean gender;
}

package com.example.entity.vo.response;

import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @program: forum
 * @description: 话题更新VO类，用于更新话题信息时的数据传输对象，只包含需要更新的字段，不包含不需要更新的字段，如话题ID等。用于接收前端传来的更新数据。后端接收到数据后，将其转换为Topic对象，然后调用service层的更新方法进行更新。这样做的好处是，可以减少不必要的数据传输，提高传输效率。同时，也可以减少不必要的数据更新，提高更新效率。
 * @author: 王贝强
 * @create: 2024-06-23 16:48
 */
@Data
public class TopicUpdateVO {
    @Min(value = 0, message = "话题ID必须大于等于0")
    int id;
    @Min(1)
    @Max(5)
    int type;
    @Length(min = 1, max = 30, message = "标题长度必须在1-30之间")
    String title;
    JSONObject content;
}

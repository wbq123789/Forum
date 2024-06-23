package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Interact;
import com.example.entity.dto.Topic;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @program: forum
 * @description: 论坛帖子mapper接口类
 * @author: 王贝强
 * @create: 2024-06-12 15:49
 */
@Mapper
public interface TopicMapper extends BaseMapper<Topic> {
    @Insert("""
            <script>
                insert ignore into topic_interact_${type} values
                <foreach collection ="interacts" item="item" separator =",">
                    (#{item.tid}, #{item.uid}, #{item.time})
                </foreach>
            </script>
            """)
    void addInteract(List<Interact> interacts, String type);
    @Delete("""
            <script>
                delete from topic_interact_${type} where
                <foreach collection="interacts" item="item" separator=" or ">
                    (tid = #{item.tid} and uid = #{item.uid})
                </foreach>
            </script>
            """)
    int deleteInteract(List<Interact> interacts, String type);
    @Select("""
            select count(*) from topic_interact_${type} where tid = #{tid}
            """)
    int interactCount(int tid, String type);

    @Select("""
            select count(*) from topic_interact_${type} where tid = #{tid} and uid = #{uid}
            """)
    int userInteractCount(int tid, int uid, String type);
}

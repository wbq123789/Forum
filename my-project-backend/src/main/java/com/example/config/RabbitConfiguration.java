package com.example.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: forum
 * @description: RabbitMQ配置类
 * @author: 王贝强
 * @create: 2024-06-10 16:23
 */
@Configuration
public class RabbitConfiguration {
    @Bean("mailQueue")
    public Queue queue(){
        return QueueBuilder
                .durable("mail")
                .build();
    }
}

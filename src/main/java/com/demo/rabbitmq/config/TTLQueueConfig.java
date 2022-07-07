package com.demo.rabbitmq.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther lkx
 * @create 2022-07-06 14:58
 * @Description:
 */
@Configuration
public class TTLQueueConfig {
    //普通交换机名称
    public static final String X_EXCHANGE = "X";
    //死信交换机名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //普通队列名称
    public static final String Queue_A = "A";
    public static final String Queue_B = "B";
    //死信队列名称
    public static final String DEAD_LETTER_Queue_D= "D";

    //声明交换机
    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }
    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //声明队列
    @Bean("QueueA")
    public Queue queueA(){
        Map<String,Object> map = new HashMap<>();
        //设置私信交换机
        map.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信RoutingKey
        map.put("x-dead-letter-routing-key","YD");
        //设置TTL（过期时间）
        map.put("x-message-ttl",10000);
        return QueueBuilder.durable(Queue_A).withArguments(map).build();
    }
    @Bean("QueueB")
    public Queue queueB(){
        Map<String,Object> map = new HashMap<>();
        //设置私信交换机
        map.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置死信RoutingKey
        map.put("x-dead-letter-routing-key","YD");
        //设置TTL（过期时间）
        map.put("x-message-ttl",40000);
        return QueueBuilder.durable(Queue_B).withArguments(map).build();
    }
    //设置死信队列
    @Bean("QueueD")
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_LETTER_Queue_D).build();
    }

    //绑定
    @Bean
    public Binding queueABindingX(@Qualifier("QueueA") Queue QueueA,@Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(QueueA).to(xExchange).with("XA");
    }
    @Bean
    public Binding queueBBindingX(@Qualifier("QueueB") Queue QueueB,@Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(QueueB).to(xExchange).with("XB");
    }
    @Bean
    public Binding queueDBindingX(@Qualifier("QueueD") Queue QueueD,@Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(QueueD).to(yExchange).with("YD");
    }
}

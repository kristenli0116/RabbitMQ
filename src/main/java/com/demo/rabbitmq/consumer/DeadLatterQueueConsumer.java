package com.demo.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @auther lkx
 * @create 2022-07-06 15:57
 * @Description:
 */
@Component
@Slf4j
public class DeadLatterQueueConsumer {
    //接收消息
    @RabbitListener(queues = "D")
    public void receiveD(Message message, Channel channel) throws Exception{
        String msg = new String(message.getBody());
        log.info("当前时间:{},收到私信队列的消息:{}",new Date().toString(),msg);
    }
}

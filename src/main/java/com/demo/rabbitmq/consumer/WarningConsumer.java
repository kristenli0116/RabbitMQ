package com.demo.rabbitmq.consumer;

import com.demo.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @auther lkx
 * @create 2022-07-07 13:08
 * @Description:
 */
@Component
@Slf4j
public class WarningConsumer {
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message){
        String s = new String(message.getBody());
        log.info("报警发现不可路由消息：{}",s);
    }

}

package com.demo.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;

/**
 * @auther lkx
 * @create 2022-07-07 10:49
 * @Description:
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback ,RabbitTemplate.ReturnsCallback {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //必须大于第三部注入spring容器，不然会报空指针异常
    @PostConstruct
    public void init(){
        //注入ConfirmCallback接口
        rabbitTemplate.setConfirmCallback(this);
        //注入ReturnsCallback接口
        rabbitTemplate.setReturnsCallback(this);
    }
    /**
     * @param correlationData
     * @param b
     * @param s
     * @deprecated 
     *          1.发消息------》交换机收到 ------》回调
     *              1.1 correlationData 保存回调消息的id及相关信息
     *              1.2 交换机收到消息 ack=true
     *              1.3 cause null
     *          2. 发消息------》交换机未收到------》回调
     *              2.1  correlationData 保存回调消息的id及相关信息
     *              2.2 交换机收到消息 ack=true
     *              2.3 失败的原因
     */
    //交换机确认回调方法
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {

        String id = correlationData != null ? correlationData.getId() : "";
        if(b){
            log.info("交换机是收到的id为：{}的消息",id);
        }else {
            log.info("交换机未收到的id为：{}的消息，由于原因：{}",id,s);
        }
    }

    //在消息传递过程中不可达目的地时将消息返回给生产者
    //只有不可达目的地的时候 才进行回退
    //ReturnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey)
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("消息：{}，被交换机{}退回，退回原因：{}，路由key：{}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getExchange(),
                returnedMessage.getReplyText(),
                returnedMessage.getRoutingKey());
    }
}

package com.zxk.rabbit.study.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 *
 * </p>
 *
 * @author zhuxiaokang
 * @date 2020/9/3 14:43
 */
@Component
public class RabbitConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitConsumer.class);

    @RabbitListener(queues = "defaultQueue")
    public void defaultQueue(Message message, Channel channel) throws IOException {
        log.info("receive: " + new String(message.getBody())+"《线程名：》"+Thread.currentThread().getName()+"《线程id:》"+Thread.currentThread().getId());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.err.println(deliveryTag);
        channel.basicAck(deliveryTag, true);
    }

}

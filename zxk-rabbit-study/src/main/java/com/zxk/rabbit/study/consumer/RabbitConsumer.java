package com.zxk.rabbit.study.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
        Map<String, Object> a =  message.getMessageProperties().getHeaders();
        log.info("receive: " + new String(message.getBody())+"《线程名：》"+Thread.currentThread().getName()+"《线程id:》"+Thread.currentThread().getId());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.err.println(deliveryTag);
        channel.basicAck(deliveryTag, true);
    }

    @RabbitListener(queues = "defaultBatchQueue", containerFactory = "simpleRabbitListenerContainerFactory")
    public void defaultQueueBatch(List<Message> messages, Channel channel) throws IOException {
        log.info("defaultQueueBatch 收到{}条message", messages.size());
        messages.forEach(message -> {
            log.info("数据: " + new String(message.getBody()));
        });
        Message message = messages.get(messages.size()-1);
        String msg = new String(message.getBody());
        long dtag = message.getMessageProperties().getDeliveryTag();
        System.err.println(dtag);
        if (msg.equals("hello99")) {
            log.info("tag {} 重回队列", dtag);
            channel.basicNack(dtag, true, true);
        }
    }

}

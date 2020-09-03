package com.zxk.rabbit.study.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * </p>
 *
 * @author zhuxiaokang
 * @date 2020/9/3 13:31
 */
@Configuration
public class RabbitConfiguration {

    private static Logger log = LoggerFactory.getLogger(RabbitConfiguration.class);

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setConfirmCallback(new ConfirmCallback());
        rabbitTemplate.setReturnCallback(new ReturnCallback());
        return rabbitTemplate;
    }

    /**
     * 消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，也就是只确认是否正确到达 Exchange
     */
    private class ConfirmCallback implements RabbitTemplate.ConfirmCallback {
        @Override
        public void confirm(CorrelationData correlationData, boolean b, String s) {
            System.out.println("ack：[{}]" + b);
            if (b) {
                System.out.println("消息到达rabbitmq服务器");
            } else {
                System.out.println("消息可能未到达rabbitmq服务器");
            }
        }
    }

    /**
     * 交换器路由不到队列时触发回调
     */
    private class ReturnCallback implements RabbitTemplate.ReturnCallback {
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            System.out.println("消息主体 message : " + message);
            System.out.println("消息主体 replyCode : " + replyCode);
            System.out.println("描述 replyText：" + replyText);
            System.out.println("消息使用的交换器 exchange : " + exchange);
            System.out.println("消息使用的路由键 routing : " + routingKey);
            log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
        }
    }

    public static final String EXCHANGE_DEFAULT = "defaultExchange";
    public static final String ROUTING_DEFAULT = "default";

    @Bean
    public Queue defaultQueue() {
        return new Queue("defaultQueue");
    }

    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(EXCHANGE_DEFAULT);
    }

    @Bean
    public Binding defaultBinding() {
        return BindingBuilder.bind(defaultQueue()).to(defaultExchange()).with("default");
    }

}

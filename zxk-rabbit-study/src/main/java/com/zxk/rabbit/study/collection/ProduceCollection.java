package com.zxk.rabbit.study.collection;

import com.zxk.rabbit.study.conf.RabbitConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author zhuxiaokang
 * @date 2020/9/3 10:53
 */
@RestController
public class ProduceCollection {
    private static final Logger log = LoggerFactory.getLogger(ProduceCollection.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @GetMapping("/helloWord")
    public String helloWord() {
        log.info("hello word");
        for (int i=0; i< 100; i++) {
            String msg = "hello" + i;
            rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_DEFAULT, RabbitConfiguration.ROUTING_DEFAULT, msg);
        }
        return "OK";
    }

    @GetMapping("/helloWordBatch")
    public String helloWordBatch() {
        log.info("hello word Batch");
        for (int i=0; i< 100; i++) {
            String msg = "hello" + i;
            rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_DEFAULT_BATCH, RabbitConfiguration.ROUTING_DEFAULT_BATCH, msg);
        }
        return "OK";
    }
}

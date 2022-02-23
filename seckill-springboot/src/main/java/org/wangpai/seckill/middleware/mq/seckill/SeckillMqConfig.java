package org.wangpai.seckill.middleware.mq.seckill;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @since 2022-3-1
 */
@Configuration
public class SeckillMqConfig {
    public static final String SECKILL_REQUEST_MQ = "seckill_request";

    @Bean
    public Queue queue() {
        return new Queue(SECKILL_REQUEST_MQ, true);
    }
}


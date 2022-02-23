package org.wangpai.seckill.middleware.mq.seckill;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;
import org.wangpai.seckill.center.service.pojo.SeckillRequest;
import org.wangpai.seckill.util.JsonUtil;

import static org.wangpai.seckill.middleware.mq.seckill.SeckillMqConfig.SECKILL_REQUEST_MQ;

/**
 * @since 2022-3-1
 */
@Component
public class SeckillMsgSender {
    private final AmqpTemplate amqpTemplate;

    public SeckillMsgSender(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public SeckillMsgSender send(SeckillRequest seckillRequest) throws JsonProcessingException {
        this.amqpTemplate.convertAndSend(SECKILL_REQUEST_MQ, JsonUtil.pojo2Json(seckillRequest));
        return this;
    }
}

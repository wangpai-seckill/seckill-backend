package org.wangpai.seckill.middleware.mq.seckill;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.wangpai.seckill.center.service.SeckillService;
import org.wangpai.seckill.center.service.pojo.SeckillRequest;
import org.wangpai.seckill.exception.CannotFindSeckillException;
import org.wangpai.seckill.exception.CannotOpSeckillException;
import org.wangpai.seckill.middleware.cache.util.CacheUtil;
import org.wangpai.seckill.util.JsonUtil;

import static org.wangpai.seckill.center.service.constant.SeckillConstant.SECKILL_REQUEST_DURATION;
import static org.wangpai.seckill.center.service.pojo.SeckillResultType.FAIL;
import static org.wangpai.seckill.center.service.pojo.SeckillResultType.SUCCESS;
import static org.wangpai.seckill.middleware.cache.type.CacheType.SECKILL_REQUEST;
import static org.wangpai.seckill.middleware.mq.seckill.SeckillMqConfig.SECKILL_REQUEST_MQ;

/**
 * @since 2022-3-1
 */
@Component
public class SeckillReceiver {
    private final SeckillService seckillService;

    public SeckillReceiver(SeckillService seckillService) {
        this.seckillService = seckillService;
    }

    @RabbitListener(queues = SECKILL_REQUEST_MQ)
    public void receive(String message) throws JsonProcessingException {
        var seckillRequest = JsonUtil.json2Pojo(message, SeckillRequest.class);
        var seckillResult = seckillRequest.getSeckillResult();
        try {
            this.seckillService.seckill(seckillRequest);
        } catch (CannotFindSeckillException | CannotOpSeckillException exception) {
            seckillResult.setResult(FAIL).setReason(exception.getMessage());
        }

        // 为了减少对象的创建，这里选择重用前面的 SeckillResult 对象
        seckillResult.setResult(SUCCESS);
        CacheUtil.set(seckillResult.getRequestId(), seckillResult, SECKILL_REQUEST, SECKILL_REQUEST_DURATION);
    }
}

package org.wangpai.seckill.center.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wangpai.seckill.center.service.pojo.SeckillProductState;
import org.wangpai.seckill.center.service.pojo.SeckillRequest;
import org.wangpai.seckill.center.service.pojo.SeckillResult;
import org.wangpai.seckill.center.util.ServiceUtil;
import org.wangpai.seckill.exception.CannotFindSeckillException;
import org.wangpai.seckill.exception.CannotOpSeckillException;
import org.wangpai.seckill.middleware.cache.type.CacheType;
import org.wangpai.seckill.middleware.cache.util.CacheUtil;
import org.wangpai.seckill.middleware.mq.seckill.SeckillMsgSender;
import org.wangpai.seckill.persistence.complex.dao.ProductDao;
import org.wangpai.seckill.persistence.origin.mapper.ProductMapper;
import org.wangpai.seckill.time.TimeUtil;
import org.wangpai.seckill.util.IdUtil;

import static org.wangpai.seckill.center.service.constant.SeckillConstant.SECKILL_REQUEST_DURATION;
import static org.wangpai.seckill.center.service.pojo.SeckillProductState.CLOSED;
import static org.wangpai.seckill.center.service.pojo.SeckillResultType.FAIL;
import static org.wangpai.seckill.center.service.pojo.SeckillResultType.SUCCESS;
import static org.wangpai.seckill.center.service.pojo.SeckillResultType.WAIT;
import static org.wangpai.seckill.middleware.cache.type.CacheType.SECKILL_PRODUCT_STATE;
import static org.wangpai.seckill.middleware.cache.type.CacheType.SECKILL_REQUEST;
import static org.wangpai.seckill.time.TimeMode.OFFLINE;

/**
 * @since 2022-2-28
 */
@Service
public class SeckillService {
    private final ProductMapper productMapper;

    private final ProductDao productDao;

    private final UserService userService;

    private final DealService dealService;

    private final SeckillMsgSender mq;

    public SeckillService(ProductMapper productMapper, ProductDao productDao,
                          UserService userService, DealService dealService,
                          SeckillMsgSender seckillMsgSender) {
        this.productMapper = productMapper;
        this.productDao = productDao;
        this.userService = userService;
        this.dealService = dealService;
        this.mq = seckillMsgSender;
    }

    /**
     * 此方法不处理重复秒杀的问题。现在决定将此问题令前端来解决
     *
     * @return 如果请求成功，将返回一个 requestId。拥有 requestId 并不意味着秒杀成功
     * @since 2022-2-28
     * @lastModified 2022-3-1
     */
    public SeckillResult makeSeckillRequest(String productId, String userPhone) throws JsonProcessingException {
        // 先尝试直接利用缓存来判断商品秒杀状态
        var productState = CacheUtil.get(productId, SECKILL_PRODUCT_STATE, SeckillProductState.class);
        // 此处不需要使用条件 productState != null，因为下面的条件中已包含
        if (productState == CLOSED) {
            return new SeckillResult().setResult(FAIL).setReason("秒杀已结束");
        }

        /**
         * 如果无法利用缓存直接判断秒杀结果，生成一个请求 ID，然后推送给 MQ 来执行请求
         */
        var requestId = IdUtil.idGenerator();
        TimeUtil.setTimeMode(OFFLINE); // 设置下面获取的是离线时间
        var requestTime = TimeUtil.getTime();
        var seckillResult = new SeckillResult()
                .setResult(WAIT) // 表示一个正在 MQ 中排队的请求
                .setRequestId(requestId)
                .setUserPhone(userPhone);
        var seckillRequest = new SeckillRequest()
                .setProductId(productId)
                .setRequestTime(requestTime)
                .setSeckillResult(seckillResult);
        this.mq.send(seckillRequest);
        // 将请求信息放在缓存，这是为了标记 requestId 的存在性，用于区分哪些请求正在 MQ 中处理，哪些 requestId 是不存在的
        CacheUtil.set(requestId, seckillResult, SECKILL_REQUEST, SECKILL_REQUEST_DURATION);

        System.out.println("makeSeckillRequest called");
        System.out.println("生成 request id：" + requestId);

        return new SeckillResult().setResult(SUCCESS).setRequestId(requestId);
    }

    /**
     * 查询秒杀结果。此方法运行前需要前端提供 cookie，否则无法查询
     *
     * @since 2022-2-28
     * @lastModified 2022-3-1
     */
    public SeckillResult getSeckillResult(HttpServletRequest request, HttpServletResponse response, String requestId)
            throws JsonProcessingException {
        var userCache = ServiceUtil.getUserFromCache(request, response);
        var user = userCache.getUser();

        if (user == null) {
            // 如果通过手机号查找不到 User，说明不是源自本人的查询
            return new SeckillResult().setResult(FAIL).setReason("未登录，查询失败");
        }
        var seckillResult = CacheUtil.get(requestId, SECKILL_REQUEST, SeckillResult.class);
        if (seckillResult == null) {
            return new SeckillResult().setResult(FAIL).setReason("该秒杀请求不存在，无法查询");
        }
        if (!Objects.equals(user.getPhone(), seckillResult.getUserPhone())) {
            // 如果通过手机号与本人不符，说明不是源自本人的查询
            return new SeckillResult().setResult(FAIL).setReason("请求非法");
        }
        return seckillResult;
    }

    /**
     * 查询秒杀结果。此方法将校验前端数据的合法性
     *
     * @since 2022-2-28
     * @lastModified 2022-3-1
     */
    public SeckillResult getSeckillResultWithoutCookies(HttpServletRequest request, HttpServletResponse response,
                                                        String userPhone, String requestId)
            throws JsonProcessingException {
        var user = this.userService.getLoginData(request, response, userPhone).getUser();
        if (user == null) {
            // 如果通过手机号查找不到 User，说明不是源自本人的查询
            return new SeckillResult().setResult(FAIL).setReason("请求非法");
        }
        var seckillResult = CacheUtil.get(requestId, SECKILL_REQUEST, SeckillResult.class);
        if (seckillResult == null) {
            return new SeckillResult().setResult(FAIL).setReason("该秒杀请求不存在，无法查询");
        }
        if (!Objects.equals(user.getPhone(), seckillResult.getUserPhone())) {
            // 如果通过手机号与本人不符，说明不是源自本人的查询
            return new SeckillResult().setResult(FAIL).setReason("请求非法");
        }
        return seckillResult;
    }

    @Transactional
    public SeckillService seckill(SeckillRequest seckillRequest)
            throws CannotFindSeckillException, CannotOpSeckillException, JsonProcessingException {
        var productId = seckillRequest.getProductId();
        var product = this.productMapper.search(productId);
        if (product == null) {
            throw new CannotFindSeckillException("找不到该商品");
        }
        if (product.getInventoryQuantity() <= 0) {
            throw new CannotOpSeckillException("商品库存不够，无法减少");
        }
        this.productDao.reduceStock(productId);
        this.dealService.createDeal(seckillRequest);
        // 如果库存为 0，在缓存中标记该商品的状态
        if (product.getInventoryQuantity() == 0) {
            CacheUtil.set(productId, CLOSED, CacheType.SECKILL_PRODUCT_STATE, SECKILL_REQUEST_DURATION);
        }
        return this;
    }
}

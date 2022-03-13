package org.wangpai.seckill.center.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wangpai.seckill.center.manager.DealManager;
import org.wangpai.seckill.center.manager.UserManager;
import org.wangpai.seckill.center.service.pojo.SeckillProductState;
import org.wangpai.seckill.center.service.pojo.SeckillRequest;
import org.wangpai.seckill.center.service.pojo.SeckillResult;
import org.wangpai.seckill.center.util.ServiceUtil;
import org.wangpai.seckill.exception.CannotFindSeckillException;
import org.wangpai.seckill.exception.CannotOpSeckillException;
import org.wangpai.seckill.middleware.cache.type.CacheType;
import org.wangpai.seckill.middleware.cache.util.CacheUtil;
import org.wangpai.seckill.middleware.lock.DistributedLockFactory;
import org.wangpai.seckill.middleware.lock.LockType;
import org.wangpai.seckill.middleware.mq.seckill.SeckillMsgSender;
import org.wangpai.seckill.persistence.complex.dao.ProductDao;
import org.wangpai.seckill.persistence.origin.domain.Product;
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

    private final UserManager userManager;

    private final DealManager dealManager;

    private final SeckillMsgSender mq;

    public SeckillService(ProductMapper productMapper, ProductDao productDao,
                          UserManager userManager, DealManager dealManager,
                          SeckillMsgSender seckillMsgSender) {
        this.productMapper = productMapper;
        this.productDao = productDao;
        this.userManager = userManager;
        this.dealManager = dealManager;
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
        var user = this.userManager.getLoginData(request, response, userPhone).getUser();
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
        final var spinTime = 1; // 自旋时间，单位：秒
        var productId = seckillRequest.getProductId();
        Product product = null;
        var lock = DistributedLockFactory.getDistributedLock(LockType.PRODUCT_LOCK, productId);
        try {
            int count = 0;
            // 获取分布式锁
            while (!lock.tryLock()) {
                try {
                    Thread.sleep(spinTime * 1000);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }

                var productState = CacheUtil.get(productId,
                        SECKILL_PRODUCT_STATE, SeckillProductState.class);
                // 此处不需要使用条件 productState != null，因为下面的条件中已包含
                if (productState == CLOSED) {
                    System.out.println("第" + (++count) + "次没有拿到锁，但秒杀已结束");
                    throw new CannotOpSeckillException("商品库存不够，无法减少");
                }

                System.out.println("第" + (++count) + "次没有拿到锁，尝试下一次");
            }
            System.out.println("得到分布式锁");

            var productState = CacheUtil.get(productId,
                    SECKILL_PRODUCT_STATE, SeckillProductState.class);
            // 此处不需要使用条件 productState != null，因为下面的条件中已包含
            if (productState == CLOSED) {
                System.out.println("得到分布式锁，但秒杀已结束");
                throw new CannotOpSeckillException("商品库存不够，无法减少");
            }

            System.out.println("得到分布式锁，秒杀可能没有结束");

            product = this.productMapper.search(productId);
            if (product == null) {
                throw new CannotFindSeckillException("找不到该商品");
            }
            int productRestNum = product.getInventoryQuantity();
            /**
             * 如果库存为 1 或 0，在缓存中标记该商品已秒杀结束。
             * 因为商品有可能一开始就为 0，所以这里也要设置一次秒杀结束状态
             */
            if (productRestNum == 1 || productRestNum == 0) {
                CacheUtil.set(productId, CLOSED, CacheType.SECKILL_PRODUCT_STATE, SECKILL_REQUEST_DURATION);
            }
            if (productRestNum < 0) {
                System.out.println("致命错误：商品超卖");
            }

            if (productRestNum <= 0) {
                throw new CannotOpSeckillException("商品库存不够，无法减少");
            }

            this.productDao.reduceStock(productId);
        } finally {

            System.out.println("尝试释放分布式锁");
            // 无论前面是否抛出异常，此处都要释放锁。这不会释放别人的锁
            lock.unlock();
        }

        // 此操作不需要上锁
        this.dealManager.createDeal(seckillRequest);

        return this;
    }
}

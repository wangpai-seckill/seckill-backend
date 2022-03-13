package org.wangpai.seckill.center.manager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wangpai.seckill.center.service.pojo.SeckillRequest;
import org.wangpai.seckill.persistence.origin.domain.Deal;
import org.wangpai.seckill.persistence.origin.mapper.DealMapper;
import org.wangpai.seckill.persistence.origin.mapper.ProductMapper;
import org.wangpai.seckill.persistence.origin.mapper.UserMapper;

/**
 * @since 2022-3-18
 */
@Service
public class DealManager {
    private final DealMapper dealMapper;

    private final UserMapper userMapper;

    private final ProductMapper productMapper;

    public DealManager(DealMapper dealMapper, UserMapper userMapper, ProductMapper productMapper) {
        this.dealMapper = dealMapper;
        this.userMapper = userMapper;
        this.productMapper = productMapper;
    }

    public DealManager createDeal(SeckillRequest seckillRequest) {
        var user = this.userMapper.searchByPhone(seckillRequest.getSeckillResult().getUserPhone());
        var product = this.productMapper.search(seckillRequest.getProductId());
        var deal = new Deal()
                .setId(seckillRequest.getSeckillResult().getRequestId())
                .setUserId(user.getId())
                .setProductId(seckillRequest.getProductId())
                .setProductPrice(product.getPrice())
                .setPurchaseTime(seckillRequest.getRequestTime());
        return this.addDeal(deal);
    }

    @Transactional
    public DealManager addDeal(Deal deal) {
        this.dealMapper.save(deal);
        return this;
    }
}

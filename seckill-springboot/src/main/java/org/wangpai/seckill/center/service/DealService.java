package org.wangpai.seckill.center.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wangpai.seckill.center.service.pojo.SeckillRequest;
import org.wangpai.seckill.persistence.complex.dao.DealDao;
import org.wangpai.seckill.persistence.complex.daodomain.SearchRequest;
import org.wangpai.seckill.persistence.origin.domain.Deal;
import org.wangpai.seckill.persistence.origin.mapper.DealMapper;
import org.wangpai.seckill.persistence.origin.mapper.ProductMapper;
import org.wangpai.seckill.persistence.origin.mapper.UserMapper;
import org.wangpai.seckill.vo.OrderListVo;

/**
 * @since 2022-2-27
 */
@Service
public class DealService {
    private final DealMapper dealMapper;

    private final DealDao dealDao;

    private final UserMapper userMapper;

    private final ProductMapper productMapper;

    public DealService(DealMapper dealMapper, DealDao dealDao, UserMapper userMapper, ProductMapper productMapper) {
        this.dealMapper = dealMapper;
        this.dealDao = dealDao;
        this.userMapper = userMapper;
        this.productMapper = productMapper;
    }

    /**
     * @since 2022-3-5
     */
    public List<OrderListVo> getDeals(SearchRequest searchRequest) {
        return this.dealDao.search(searchRequest);
    }

    public DealService createDeal(SeckillRequest seckillRequest) {
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
    public DealService addDeal(Deal deal) {
        this.dealMapper.save(deal);
        return this;
    }
}

package org.wangpai.seckill.center.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.wangpai.seckill.persistence.complex.dao.DealDao;
import org.wangpai.seckill.persistence.complex.daodomain.SearchRequest;
import org.wangpai.seckill.vo.OrderListVo;

/**
 * @since 2022-2-27
 */
@Service
public class DealService {
    private final DealDao dealDao;

    public DealService(DealDao dealDao) {
        this.dealDao = dealDao;
    }

    /**
     * @since 2022-3-5
     */
    public List<OrderListVo> getDeals(SearchRequest searchRequest) {
        return this.dealDao.search(searchRequest);
    }
}

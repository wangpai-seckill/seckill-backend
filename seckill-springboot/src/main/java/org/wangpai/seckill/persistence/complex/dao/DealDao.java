package org.wangpai.seckill.persistence.complex.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.wangpai.seckill.persistence.complex.daodomain.SearchRequest;
import org.wangpai.seckill.vo.OrderListVo;

/**
 * @since 2022-3-5
 */
@Mapper
public interface DealDao {
    /**
     * 注意：此处 SQL 的多表连接中，是表 Deal 分别与 User、Product 相连，
     * 而不是先将 User 与 Product 相连，然后与 Deal 相连
     *
     * @return 如果没有搜索到，此方法会返回 null，而不是抛出异常。
     * 如果 num （搜索的条数）值过大，此方法也不会引发异常，而尽可能获取更多的数据
     * @since 2022-3-5
     */
    @Select(value = "SELECT userName, Product.price productPrice, purchaseTime, " +
            "Deal.id AS orderId, Product.image AS productImage " +
            "FROM Deal, Product, User " +
            "WHERE Deal.userId=User.id AND Deal.productId=Product.id " +
            "AND User.phone=#{userPhone} " +
            "ORDER BY Deal.purchaseTime DESC LIMIT #{begin}, #{num}")
    List<OrderListVo> search(SearchRequest searchRequest);
}

package org.wangpai.seckill.persistence.origin.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.wangpai.seckill.persistence.origin.domain.Deal;

/**
 * @since 2022-2-25
 */
@Mapper
public interface DealMapper {
    /**
     * 如果没有搜索到，此方法会返回 null，而不是抛出异常
     *
     * @since 2022-2-25
     */
    @Select(value = "SELECT * FROM Deal WHERE id = #{id}")
    Deal search(String id);

    /**
     * @since 2022-2-25
     */
    @Insert(value = "INSERT INTO Deal (id, productPrice, purchaseTime, userId, productId) " +
            "VALUES (#{id}, #{productPrice}, #{purchaseTime}, #{userId}, #{productId})")
    int save(Deal deal);
}

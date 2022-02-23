package org.wangpai.seckill.persistence.complex.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 注意：Mapper 中的方法不能重载，这与形参个数无关。
 *
 * @since 2022-2-28
 */
@Mapper
public interface ProductDao {
    /**
     * @since 2022-2-28
     */
    @Update("update Product set inventoryQuantity = inventoryQuantity - 1" +
            " where id = #{productId}")
    int reduceStock(String productId);
}

package org.wangpai.seckill.persistence.origin.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.wangpai.seckill.persistence.origin.domain.Product;

/**
 * @since 2022-2-25
 */
@Mapper
public interface ProductMapper {
    /**
     * 如果没有搜索到，此方法会返回 null，而不是抛出异常
     *
     * @since 2022-2-25
     */
    @Select(value = "SELECT * FROM Product WHERE id = #{id}")
    Product search(String id);

    /**
     * @since 2022-2-25
     */
    @Insert(value = "INSERT INTO Product (id, title, image, description, price, " +
            "startTime, endTime, inventoryQuantity) " +
            "VALUES (#{id}, #{title}, #{image}, #{description}, " +
            "#{price}, #{startTime}, #{endTime}, #{inventoryQuantity})")
    int save(Product product);
}

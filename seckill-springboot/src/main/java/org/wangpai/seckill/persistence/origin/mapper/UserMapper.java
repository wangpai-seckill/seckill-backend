package org.wangpai.seckill.persistence.origin.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.dao.DuplicateKeyException;
import org.wangpai.seckill.persistence.origin.domain.User;

/**
 * @since 2022-2-25
 */
@Mapper
public interface UserMapper {
    /**
     * 如果没有搜索到，此方法会返回 null，而不是抛出异常
     *
     * @since 2022-2-25
     */
    @Select(value = "SELECT * FROM User WHERE id = #{id}")
    User searchById(String id);

    /**
     * 如果没有搜索到，此方法会返回 null，而不是抛出异常
     *
     * @since 2022-2-25
     */
    @Select(value = "SELECT * FROM User WHERE phone = #{phone}")
    User searchByPhone(String phone);

    /**
     *
     * @throws DuplicateKeyException 如果因主键重复而添加失败，会抛此异常
     * @since 2022-2-25
     */
    @Insert(value = "INSERT INTO User (id, phone, userName, password) " +
            "VALUES (#{id}, #{phone}, #{userName}, #{password})")
    int save(User user);
}

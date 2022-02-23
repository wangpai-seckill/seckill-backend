package org.wangpai.seckill.middleware.cache.type;

import lombok.Getter;
import org.wangpai.seckill.center.service.pojo.SeckillProductState;
import org.wangpai.seckill.center.service.pojo.SeckillRequest;
import org.wangpai.seckill.persistence.origin.domain.User;

/**
 * @since 2022-2-24
 */
public enum CacheType {
    USER("user", User.class), // v
    SECKILL_REQUEST("seckill_request", SeckillRequest.class), // v
    SECKILL_PRODUCT_STATE("seckill_product_state", SeckillProductState.class), // v

    VISITS("visits"),
    PRODUCT("product"),
    PRODUCT_LIST("product_list"),
    MQ_HANDLE_RESULT("mq_handle_result"),
    DEAL("deal"),
    ;

    @Getter
    private String name;

    /**
     * 返回结果所关联的枚举类型
     *
     * @since 2022-2-28
     */
    @Getter
    private Class<?> associatedType;

    CacheType(String name) {
        this.name = name;
    }

    CacheType(String name, Class<?> associatedType) {
        this.name = name;
        this.associatedType = associatedType;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

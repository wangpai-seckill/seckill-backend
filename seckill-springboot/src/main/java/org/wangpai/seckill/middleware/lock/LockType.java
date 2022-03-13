package org.wangpai.seckill.middleware.lock;

import lombok.Getter;

/**
 * @since 2022-2-24
 */
public enum LockType {
    PRODUCT_LOCK("product_lock"),
    ;

    @Getter
    private String name;

    LockType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

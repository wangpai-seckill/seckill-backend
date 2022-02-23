package org.wangpai.seckill.center.response;

import lombok.Getter;

public enum DataType {
    NULL(null),

    STRING(String.class),

    BINARY(byte[].class),

    JSON_OBJECT(String.class),
    ;

    @Getter
    private final Class<?> classType;

    DataType(Class<?> classType) {
        this.classType = classType;
    }
}
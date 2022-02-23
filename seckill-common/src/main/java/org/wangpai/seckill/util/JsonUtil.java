package org.wangpai.seckill.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @since 2022-1-4
 */
public class JsonUtil {
    /**
     * 此对象的方法是线程安全的
     *
     * @since 2022-1-4
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 提供对日期转换的支持
        MAPPER.registerModule(new JavaTimeModule());
    }

    /**
     * 如果 pojo 为 null，不报错，结果为 null
     *
     * @since 2022-1-4
     * @lastModified 2022-2-28
     */
    public static <T> String pojo2Json(T pojo) throws JsonProcessingException {
        return MAPPER.writeValueAsString(pojo);
    }

    /**
     * 如果 json 为 null，不报错，结果为 null
     *
     * @since 2022-1-4
     * @lastModified 2022-2-28
     */
    public static <T> T json2Pojo(String json, Class<T> realType) throws JsonProcessingException {
        if (json == null) {
            return null;
        }
        return MAPPER.readValue(json, realType);
    }
}

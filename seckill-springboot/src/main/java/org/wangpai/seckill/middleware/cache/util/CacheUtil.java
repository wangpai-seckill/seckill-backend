package org.wangpai.seckill.middleware.cache.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;
import org.wangpai.seckill.middleware.cache.type.CacheType;
import org.wangpai.seckill.middleware.cache.type.CacheTypeUtil;
import org.wangpai.seckill.util.JsonUtil;

/**
 * 此类的初始化依赖于 Spring 容器。Spring 容器不启动，不能使用本类的静态方法
 *
 * @since 2022-2-24
 */
@Component
public class CacheUtil {
    private static RedisUtil redisUtil;

    public CacheUtil(RedisUtil redis) {
        redisUtil = redis;
    }

    public static <T> T get(String originKey, CacheType cacheType, Class<T> convertedType)
            throws JsonProcessingException {
        var objJson = redisUtil.get(keyCompound(cacheType, originKey));
        return JsonUtil.json2Pojo(objJson, convertedType);
    }

    /**
     * @since 2022-2-24
     * @deprecated 2022-2-26 此方法没有设置过期时间，应当废弃
     */
    @Deprecated
    public static void set(String originKey, Object value, CacheType cacheType) throws JsonProcessingException {
        redisUtil.set(keyCompound(cacheType, originKey), JsonUtil.pojo2Json(value));
    }

    /**
     * timeout 代表值多久过期，单位为秒
     *
     * @since 2022-2-24
     */
    public static void set(String originKey, Object value, CacheType cacheType, long timeout)
            throws JsonProcessingException {
        redisUtil.set(keyCompound(cacheType, originKey), JsonUtil.pojo2Json(value), timeout, TimeUnit.SECONDS);
    }

    public static boolean del(String originKey, CacheType cacheType) {
        return redisUtil.del(keyCompound(cacheType, originKey));
    }

    public static void hset(String originLevel1Key, String originLevel2Key, Object value,
                            CacheType cacheType, long timeout) throws JsonProcessingException {
        redisUtil.hset(keyCompound(cacheType, originLevel1Key), originLevel2Key,
                JsonUtil.pojo2Json(value), timeout);
    }

    public static <T> T hget(String originLevel1Key, String originLevel2Key,
                             CacheType cacheType, Class<T> convertedType) throws JsonProcessingException {
        var objJson = redisUtil.hget(keyCompound(cacheType, originLevel1Key), originLevel2Key);
        return JsonUtil.json2Pojo(objJson, convertedType);
    }

    private static String keyCompound(CacheType cacheType, String originKey) {
        return CacheTypeUtil.generatePrefix(cacheType) + "-" + originKey;
    }
}

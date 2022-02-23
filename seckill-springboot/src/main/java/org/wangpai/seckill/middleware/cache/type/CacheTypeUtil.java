package org.wangpai.seckill.middleware.cache.type;

public class CacheTypeUtil {
    public static String generatePrefix(CacheType cacheType) {
        return cacheType.toString();
    }
}

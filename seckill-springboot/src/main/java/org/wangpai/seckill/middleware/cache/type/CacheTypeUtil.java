package org.wangpai.seckill.middleware.cache.type;

public class CacheTypeUtil {
    public static String generatePrefix(CacheType cacheType) {
        return cacheType.toString();
    }

    public static String keyCompound(CacheType cacheType, String originKey) {
        return CacheTypeUtil.generatePrefix(cacheType) + "-" + originKey;
    }
}

package org.wangpai.seckill.middleware.cache.util;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * 此类的初始化依赖于 Spring 容器。Spring 容器不启动，不能使用本类的静态方法
 *
 * @since 2022-2-24
 */
@Component
public class RedisUtil implements InitializingBean {
    public final TimeUnit DEFAULT_EXPIRE_TIME_UNIT = TimeUnit.SECONDS;

    private final RedisTemplate<String, String> redis;

    private ValueOperations<String, String> stringOps;

    private HashOperations<String, String, String> hashOps;

    public RedisUtil(RedisTemplate<String, String> redisTemplate) {
        this.redis = redisTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        this.stringOps = this.redis.opsForValue();
        this.hashOps = this.redis.opsForHash();
    }

    public String get(String key) {
        return this.stringOps.get(key);
    }

    /**
     * @since 2022-2-24
     * @deprecated 2022-2-26 此方法没有设置过期时间，应当废弃
     */
    @Deprecated
    public RedisUtil set(String key, String value) {
        this.stringOps.set(key, value);
        return this;
    }

    public RedisUtil set(String key, String value, long timeout, TimeUnit unit) {
        this.stringOps.set(key, value, timeout, unit);
        return this;
    }

    /**
     * @param key ：Hash 表的名称
     * @param field ：Hash 表中，某键值对中的键
     * @param value ：Hash 表中，某键值对中的值
     * @param timeout ：表名为 key 整个 Hash 表的过期时间，单位为 DEFAULT_EXPIRE_TIME_UNIT
     * @since 2022-3-1
     */
    public RedisUtil hset(String key, String field, String value, long timeout) {
        return this.hset(key, field, value, timeout, this.DEFAULT_EXPIRE_TIME_UNIT);
    }

    /**
     * @param key ：Hash 表的名称
     * @param field ：Hash 表中，某键值对中的键
     * @param value ：Hash 表中，某键值对中的值
     * @param timeout ：表名为 key 整个 Hash 表的过期时间，单位为 unit
     * @param unit ：timeout 对应的时间单位
     * @since 2022-3-1
     */
    public RedisUtil hset(String key, String field, String value, long timeout, TimeUnit unit) {
        this.hashOps.put(key, field, value);
        this.redis.expire(key, timeout, unit);
        return this;
    }

    public String hget(String key, String field) {
        return this.hashOps.get(key, field);
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(this.redis.hasKey(key));
    }

    public boolean expire(String key, long timeout, TimeUnit unit) {
        return Boolean.TRUE.equals(this.redis.expire(key, timeout, unit));
    }

    public boolean expire(String key, long timeout) {
        return Boolean.TRUE.equals(this.redis.expire(key, timeout, this.DEFAULT_EXPIRE_TIME_UNIT));
    }

    public boolean del(String key) {
        return Boolean.TRUE.equals(this.redis.delete(key));
    }

    public long incr(String key) {
        return this.stringOps.increment(key);
    }

    public long decr(String key) {
        return this.stringOps.decrement(key);
    }
}

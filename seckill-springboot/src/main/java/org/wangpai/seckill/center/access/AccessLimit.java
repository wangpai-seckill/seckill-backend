package org.wangpai.seckill.center.access;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 对控制器的访问限制
 *
 * @since 2022-2-23
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
    /**
     * 是否需要登录
     *
     * @since 2022-2-23
     */
    boolean needLogin() default true;

    /**
     * 用户本次访问状态的有效时间
     *
     * @since 2022-2-23
     */
    int duration() default -1;

    /**
     * 在 limitTime 时间内，同一用户的累计的最大访问量
     *
     * @since 2022-2-23
     */
    int maxVisits() default -1;

    /**
     * maxVisits 的时间统计依据，单位秒
     *
     * @since 2022-2-23
     */
    int limitTime() default -1;
}

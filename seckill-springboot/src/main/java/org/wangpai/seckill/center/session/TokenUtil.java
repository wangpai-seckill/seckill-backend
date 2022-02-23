package org.wangpai.seckill.center.session;

import org.wangpai.seckill.util.IdUtil;

/**
 * 这里，token 代表每个 session 的 ID
 *
 * @since 2022-2-24
 */
public class TokenUtil {
    public static final String TOKEN_NAME = "token";

    /**
     * token 的过期时间，单位秒
     *
     * @since 2022-2-24
     */
    public static final int TOKEN_TIMEOUT = 1800;

    public static String generateToken() {
        return IdUtil.idGenerator();
    }
}

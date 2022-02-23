package org.wangpai.seckill.center.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @since 2022-2-25
 */
public class SessionUtil {
    public static String getSessionId(HttpServletRequest request) {
        return CookieUtil.getCookie(request, CookieType.TOKEN);
    }

    /**
     * @return session 的 ID
     * @since 2022-2-25
     */
    public static String addSessionId(HttpServletResponse response) {
        var token = TokenUtil.generateToken();
        CookieUtil.addCookie(response, CookieType.TOKEN, token);

        return token;
    }
}

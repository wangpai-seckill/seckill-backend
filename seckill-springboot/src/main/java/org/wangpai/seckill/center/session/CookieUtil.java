package org.wangpai.seckill.center.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.wangpai.seckill.center.session.TokenUtil.TOKEN_NAME;
import static org.wangpai.seckill.center.session.TokenUtil.TOKEN_TIMEOUT;

public class CookieUtil {
    /**
     * 注意：此方法的第一个形参是 request，而不是 response
     *
     * @since 2022-2-25
     */
    public static String getCookie(HttpServletRequest request, CookieType cookieType) {
        String tokenName = switch (cookieType) {
            case TOKEN -> TOKEN_NAME;
            default -> {
                // 敬请期待
                yield null;
            }
        };

        var cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(tokenName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 注意：此方法的第一个形参是 response，而不是 request
     *
     * @since 2022-2-25
     */
    public static void addCookie(HttpServletResponse response, CookieType cookieType, String value) {
        switch (cookieType) {
            case TOKEN -> {
                var cookie = new Cookie(TOKEN_NAME, value);
                cookie.setMaxAge(TOKEN_TIMEOUT);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            default -> {
                // 敬请期待
            }
        }
    }
}

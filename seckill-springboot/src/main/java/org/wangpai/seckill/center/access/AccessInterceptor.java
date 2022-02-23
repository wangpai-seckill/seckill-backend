package org.wangpai.seckill.center.access;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.wangpai.seckill.center.response.RspMsg;
import org.wangpai.seckill.center.response.RspUtil;
import org.wangpai.seckill.center.session.SessionUtil;
import org.wangpai.seckill.middleware.cache.type.CacheType;
import org.wangpai.seckill.middleware.cache.util.CacheUtil;
import org.wangpai.seckill.persistence.origin.domain.User;

import static org.wangpai.seckill.center.response.RspState.ERROR;

/**
 * @since 2022-2-25
 */
@Component
public class AccessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws IOException {
        var sessionId = SessionUtil.getSessionId(request);
        if (sessionId == null || sessionId.equals("")) {
            SessionUtil.addSessionId(response);
        }

        if (handler instanceof HandlerMethod) {
            var hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null || !accessLimit.needLogin()) {
                return true;
            }

            var user = CacheUtil.get(sessionId, CacheType.USER, User.class);

            if (user == null) {
                RspUtil.setResponse(response, new RspMsg().setState(ERROR).setMsg("未登录或用户信息失效"));
                return false;
            }

            var duration = accessLimit.duration();
            var maxVisits = accessLimit.maxVisits();
            var limitTime = accessLimit.limitTime();
            // 敬请期待
        }

        return true;
    }

}
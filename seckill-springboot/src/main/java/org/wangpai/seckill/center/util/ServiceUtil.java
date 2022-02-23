package org.wangpai.seckill.center.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.wangpai.seckill.center.service.pojo.UserCache;
import org.wangpai.seckill.center.session.SessionUtil;
import org.wangpai.seckill.middleware.cache.type.CacheType;
import org.wangpai.seckill.middleware.cache.util.CacheUtil;
import org.wangpai.seckill.persistence.origin.domain.User;

/**
 * @since 2022-3-4
 */
@Service
public class ServiceUtil {
    private static CacheUtil cacheUtil;

    public ServiceUtil(CacheUtil cacheUtil) {
        ServiceUtil.cacheUtil = cacheUtil;
    }

    /**
     * 副作用：如果请求中没有相应 cookie，将会创建一个
     *
     * @return 无论能不能找到 User，其返回的 sessionId 字段都不会为 null
     * @since 2022-3-4
     */
    public static UserCache getUserFromCache(HttpServletRequest request, HttpServletResponse response)
            throws JsonProcessingException {
        var userCache = new UserCache();
        // 先尝试从缓存中读取 session ID
        var sessionId = SessionUtil.getSessionId(request);
        if (sessionId != null && !sessionId.equals("")) {
            // 从缓存中读取 User
            return userCache.setUser(getUserFromCache(sessionId)).setSessionId(sessionId);
        } else {
            // 如果 sessionId 不存在，则创建一个
            return userCache.setSessionId(SessionUtil.addSessionId(response));
        }
    }

    /**
     * @since 2022-3-4
     */
    private static User getUserFromCache(String sessionId) throws JsonProcessingException {
        return cacheUtil.get(sessionId, CacheType.USER, User.class);
    }
}

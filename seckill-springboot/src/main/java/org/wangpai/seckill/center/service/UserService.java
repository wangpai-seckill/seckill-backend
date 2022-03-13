package org.wangpai.seckill.center.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wangpai.seckill.center.session.CookieType;
import org.wangpai.seckill.center.session.CookieUtil;
import org.wangpai.seckill.center.session.SessionUtil;
import org.wangpai.seckill.middleware.cache.type.CacheType;
import org.wangpai.seckill.middleware.cache.util.CacheUtil;
import org.wangpai.seckill.persistence.origin.domain.User;
import org.wangpai.seckill.persistence.origin.mapper.UserMapper;
import org.wangpai.seckill.util.IdUtil;
import org.wangpai.seckill.vo.RegistrationVo;

import static org.wangpai.seckill.center.session.TokenUtil.TOKEN_TIMEOUT;

/**
 * @since 2022-2-25
 */
@Service
public class UserService {
    private final UserMapper userMapper;

    private final CacheUtil cacheUtil;

    public UserService(UserMapper userMapper, CacheUtil cacheUtil) {
        this.userMapper = userMapper;
        this.cacheUtil = cacheUtil;
    }

    public UserService resetLoginCache(HttpServletResponse response, User user, String sessionId)
            throws JsonProcessingException {
        // 将 User 置于缓存或重置缓存过期时间
        this.setUserToCache(sessionId, user);
        // 重置 Cookie 过期时间
        CookieUtil.addCookie(response, CookieType.TOKEN, sessionId);
        return this;
    }

    /**
     * 如果 User 添加成功，此方法会将其缓存，并重置 Cookie
     *
     * @since 2022-2-26
     */
    @Transactional
    public UserService addUser(HttpServletRequest request, HttpServletResponse response,
                               RegistrationVo registrationVo) throws JsonProcessingException {
        var user = new User()
                .setId(IdUtil.idGenerator())
                .setPhone(registrationVo.getPhone())
                .setUserName(registrationVo.getUserName())
                .setPassword(registrationVo.getPassword());
        this.userMapper.save(user);

        // 如果上面的数据库保存操作没有抛出异常，将 User 置入缓存，并刷新 Cookie
        var sessionId = SessionUtil.getSessionId(request);
        if (sessionId == null || sessionId.equals("")) {
            // 如果 sessionId 不存在，则创建一个
            sessionId = SessionUtil.addSessionId(response);
        }

        this.resetLoginCache(response, user, sessionId);
        return this;
    }
    
    public UserService setUserToCache(String sessionId, User user) throws JsonProcessingException {
        this.cacheUtil.set(sessionId, user, CacheType.USER, TOKEN_TIMEOUT);
        return this;
    }
}

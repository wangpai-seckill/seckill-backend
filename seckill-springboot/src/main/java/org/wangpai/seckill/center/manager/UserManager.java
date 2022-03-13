package org.wangpai.seckill.center.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.wangpai.seckill.center.service.pojo.UserCache;
import org.wangpai.seckill.center.util.ServiceUtil;
import org.wangpai.seckill.persistence.origin.domain.User;
import org.wangpai.seckill.persistence.origin.mapper.UserMapper;

/**
 * @since 2022-3-18
 */
@Service
public class UserManager {
    private final UserMapper userMapper;

    public UserManager(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 如果没有找到 Session ID，则会创建一个，并将其添加至 Cookie 中
     *
     * @return 此方法不会返回 null。如果 loginVo 中的手机号有误，将导致找不到 User，此时返回的 user 字段会为 null
     * @since 2022-2-26
     */
    public UserCache getLoginData(HttpServletRequest request, HttpServletResponse response, String userPhone)
            throws JsonProcessingException {
        var userCache = ServiceUtil.getUserFromCache(request, response);
        var user = userCache.getUser();
        var sessionId = userCache.getSessionId();

        // 如果缓存中没有，则从数据库中读取 User
        if (user == null) {
            user = this.getUserFromDb(userPhone);
            if (user == null) {
                return new UserCache();
            }
        }

        return new UserCache(sessionId, user);
    }

    public User getUserFromDb(String phone) {
        return this.userMapper.searchByPhone(phone);
    }
}

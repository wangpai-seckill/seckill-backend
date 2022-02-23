package org.wangpai.seckill.center.service.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.seckill.persistence.origin.domain.User;

/**
 * @since 2022-2-28
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class UserCache {
    private String sessionId;

    private User user;

    public UserCache() {
        super();
    }

    public UserCache(String sessionId, User user) {
        super();
        this.sessionId = sessionId;
        this.user = user;
    }
}

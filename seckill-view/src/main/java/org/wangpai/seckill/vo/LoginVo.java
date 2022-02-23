package org.wangpai.seckill.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @since 2022-2-23
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class LoginVo {
    private String phone;

    private String password;
}

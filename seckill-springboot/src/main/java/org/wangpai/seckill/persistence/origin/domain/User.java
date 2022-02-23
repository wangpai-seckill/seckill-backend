package org.wangpai.seckill.persistence.origin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class User {
    private String id;

    private String phone;

    private String userName;

    private String password;
}

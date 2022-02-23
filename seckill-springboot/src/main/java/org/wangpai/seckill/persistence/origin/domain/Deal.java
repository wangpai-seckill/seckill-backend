package org.wangpai.seckill.persistence.origin.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 订单。因为【订单】的英文【Order】是数据库的关键字，所以这里换成了【Deal】
 *
 * @since 2022-2-25
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Deal {
    private String id;

    private String userId;

    private String productId;

    private int productPrice;

    private LocalDateTime purchaseTime;
}

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
public class OrderListVo {
    private String userName;

    private String productPrice;

    private String purchaseTime;

    private String orderId;

    @ToString.Exclude
    private byte[] productImage; // 调用 toString 时，不打印此项，因为这无法打印，强行打印会占据巨大的文字输出面积
}

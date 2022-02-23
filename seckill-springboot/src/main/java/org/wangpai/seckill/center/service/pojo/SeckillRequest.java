package org.wangpai.seckill.center.service.pojo;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @since 2022-2-28
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class SeckillRequest {
    private String productId;

    private LocalDateTime requestTime;

    private SeckillResult seckillResult;
}

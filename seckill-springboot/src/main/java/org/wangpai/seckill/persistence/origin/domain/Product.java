package org.wangpai.seckill.persistence.origin.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @since 2022-2-25
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Product {
    private String id;

    private String title;

    @ToString.Exclude
    private byte[] image; // 调用 toString 时，不打印此项，因为这无法打印，强行打印会占据巨大的文字输出面积

    private String description;

    private int price;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int inventoryQuantity;
}

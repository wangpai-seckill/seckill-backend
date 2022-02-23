package org.wangpai.seckill.persistence.complex.daodomain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @since 2022-3-5
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class SearchRequest {
    private String userPhone;

    private int begin = 0;

    private int num = 5;
}

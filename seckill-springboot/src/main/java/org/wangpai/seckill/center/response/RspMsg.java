package org.wangpai.seckill.center.response;

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
public class RspMsg {
    private String msg;

    private RspState state;

    /**
     * 此字段只有在 state 为 SUCCESS 时才可能有效
     *
     * @since 2022-2-25
     */
    private DataType dataType;

    /**
     * 此字段只有在 state 为 SUCCESS 时才可能有效
     *
     * @since 2022-2-25
     */
    private Object data;
}

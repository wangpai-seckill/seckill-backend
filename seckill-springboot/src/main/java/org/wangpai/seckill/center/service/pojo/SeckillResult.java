package org.wangpai.seckill.center.service.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @since 2022-3-1
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class SeckillResult {
    private SeckillResultType result;

    private String reason;

    // 这个字段是必须的，否则无法向前端反馈 requestId
    private String requestId;

    // 这个字段是必须的，否则无法检验前端查询请求是否非法
    private String userPhone;

    public SeckillResult() {
        super();
    }

    public SeckillResult(SeckillResultType result) {
        super();
        this.result = result;
    }
}

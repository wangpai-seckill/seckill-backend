package org.wangpai.seckill.time;

import java.time.LocalDateTime;

/**
 * 此类用于获取非联网的本地时间
 *
 * @since 2021-11-21
 */
public class OfflineTimeUtil {
    public static LocalDateTime getTime() {
        return LocalDateTime.now();
    }
}

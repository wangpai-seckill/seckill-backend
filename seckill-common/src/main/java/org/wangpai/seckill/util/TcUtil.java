package org.wangpai.seckill.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * TC：Type Conversion 类型转换
 *
 * @since 2022-3-3
 */
public class TcUtil {
    /**
     * @since 2021-11-24
     */
    public static InputStream byteArray2InputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 此方法不会重置流，因此经过转化后，原来的流将不能再使用。
     * 因此，如果想要反复读取流，请转化前对流进行备份
     *
     * @since 2021-11-24
     */
    public static byte[] inputStream2byteArray(InputStream inputStream) throws IOException {
        return inputStream.readAllBytes();
    }

    /**
     * @since 2021-11-24
     */
    public static byte[] file2byteArray(File file) throws IOException {
        return inputStream2byteArray(new FileInputStream(file));
    }
}

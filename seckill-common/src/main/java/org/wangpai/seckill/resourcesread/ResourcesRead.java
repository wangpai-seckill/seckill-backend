package org.wangpai.seckill.resourcesread;

import java.io.IOException;
import java.io.InputStream;
import org.wangpai.seckill.util.TcUtil;

public class ResourcesRead {
    /**
     * 通过路径读取图片文件
     *
     * @param path 以 resource 的路径为基准，不需要以 / 为开头
     * @since 2022-3-3
     */
    public static byte[] readImage2ByteArray(String path) throws IOException {
        return TcUtil.inputStream2byteArray(readImage2InputStream(path));
    }

    /**
     * 通过路径读取图片文件
     *
     * @param path 以 resource 的路径为基准，不需要以 / 为开头
     * @since 2021-11-24
     */
    public static InputStream readImage2InputStream(String path) {
        /**
         * 方法 getResourceAsStream 的路径是以资源目录 resources 为基准的，
         * 且不受模块的限制。这于 xxx.class 中 xxx 是哪个模块的哪个类无关
         */
        var imageStream = ResourcesRead.class.getClassLoader()
                .getResourceAsStream(path);
        if (imageStream == null) {
            System.out.println("--readImage 为 null--：" + path); // FIXME 日志
        }
        return imageStream;
    }
}

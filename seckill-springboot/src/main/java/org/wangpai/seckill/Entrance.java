package org.wangpai.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @since 2022-2-23
 */
@SpringBootApplication(scanBasePackages = {"org.wangpai.seckill"})
public class Entrance {
    public static void main(String[] args) {
        SpringApplication.run(Entrance.class, args);
    }
}

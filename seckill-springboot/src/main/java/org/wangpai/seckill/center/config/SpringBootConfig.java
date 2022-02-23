package org.wangpai.seckill.center.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.wangpai.seckill.center.access.AccessInterceptor;

@Configuration
public class SpringBootConfig implements WebMvcConfigurer {
    private final AccessInterceptor accessInterceptor;

    public SpringBootConfig(AccessInterceptor accessInterceptor) {
        this.accessInterceptor = accessInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.accessInterceptor);
    }
}

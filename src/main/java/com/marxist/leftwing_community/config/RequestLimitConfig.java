package com.marxist.leftwing_community.config;

import com.marxist.leftwing_community.interceptor.RequestLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 注册限制请求次数拦截器
 */
@Configuration
public class RequestLimitConfig implements WebMvcConfigurer {

    /**
     * 添加requestLimitInterceptor
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new RequestLimitInterceptor());

        //拦截前台功能请求
        registration.addPathPatterns("/home", "/article_list", "/article", "library", "/author_index", "/literature");
        registration.excludePathPatterns("/error");
    }

}

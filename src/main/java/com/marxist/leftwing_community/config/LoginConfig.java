package com.marxist.leftwing_community.config;

import com.marxist.leftwing_community.interceptor.LoginInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class LoginConfig implements WebMvcConfigurer {

    /**
     * 配置登录拦截器
     *
     * @param registry 拦截注册器
     */
    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        //注册登录拦截器
        InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor());

        //拦截admin地址开头的请求
        registration.addPathPatterns("/admin/**");
        //添加不拦截路径:登录访问页
        registration.excludePathPatterns("/admin/login_page","/admin/login");

    }

    /**
     * 配置静态访问资源
     */
    @Component
    static
    class WebMvcConfigureAdapter extends WebMvcConfigurerAdapter {
        //不拦截标识为静态资源的请求
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/static/**")
                    .addResourceLocations("classpath:/static/");
            super.addResourceHandlers(registry);
        }
    }

}

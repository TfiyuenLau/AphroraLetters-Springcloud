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
     * @param registry 拦截器
     */
    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        //注册登录拦截器
        InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor());

        //registration.addPathPatterns("/**");//所有路径都被拦截
        // 拦截admin地址开头的请求
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

        /**
         * 配置不拦截static下的静态资源
         *
         * @param registry
         */
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/static/**")
                    .addResourceLocations("classpath:/static/");
            super.addResourceHandlers(registry);
        }
    }

}

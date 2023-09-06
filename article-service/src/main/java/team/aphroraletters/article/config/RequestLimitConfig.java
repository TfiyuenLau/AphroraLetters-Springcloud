package team.aphroraletters.article.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import team.aphroraletters.article.interceptor.CommentLimitInterceptor;
import team.aphroraletters.article.interceptor.VisitLimitInterceptor;

/**
 * 注册限制请求次数拦截器
 */
@Configuration
public class RequestLimitConfig implements WebMvcConfigurer {

    /**
     * 添加限制请求拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加访问拦截器
        InterceptorRegistration visitRegistration = registry.addInterceptor(new VisitLimitInterceptor());
        visitRegistration.addPathPatterns("/home", "/article_list", "/article", "library", "/author_index", "/literature");//拦截前台功能请求
        visitRegistration.excludePathPatterns("/error");

        //添加评论拦截器
        InterceptorRegistration commentRegistration = registry.addInterceptor(new CommentLimitInterceptor());
        commentRegistration.addPathPatterns("/article/insert");//拦截添加评论请求
    }

}

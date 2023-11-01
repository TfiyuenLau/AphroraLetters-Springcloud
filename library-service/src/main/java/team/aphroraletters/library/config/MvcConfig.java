package team.aphroraletters.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfigurer配置类
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 标识为静态资源的请求不拦截
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // 文件上传映射地址:addResourceLocations是我们的文件上传绝对路径，注意要加file:
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:/resources/static/img/", "file:/app/file:/app/al-library.jar!/BOOT-INF/classes!/static/img/");
        registry.addResourceHandler("/pdf/**")
                .addResourceLocations("file:/resources/static/pdf/", "file:/app/file:/app/al-library.jar!/BOOT-INF/classes!/static/pdf/");
    }

}

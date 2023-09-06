package team.aphroraletters.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

//跨域配置类
@Configuration
public class CorsConfig {

    /**
     * 配置过滤器CorsFilter
     * 解决PDF.js跨域无法访问
     *
     * @return CorsFilter
     */
    @Bean
    CorsFilter corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://8.130.39.9:8080/"));//允许的域,写*时cookie可能无法使用
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));//允许的请求方式
        configuration.setAllowedHeaders(Collections.singletonList("*"));//允许的头信息
        configuration.setAllowCredentials(true);//是否允许请求带有验证信息

        //2.添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }

}

package com.marxist.leftwing_community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.marxist.leftwing_community.dao")//SpringBoot启动类中开启mapper接口的扫描
public class LeftwingCommunityApplication extends SpringBootServletInitializer {
    //SpringBoot入口类
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(LeftwingCommunityApplication.class, args);

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String property = environment.getProperty("spring.application.name");
        System.out.println("current spring.application.name="+property);
        System.out.println("current server-address=http://localhost:8080/home");
        System.out.println("current server-test-address=http://localhost:8080/admin/starter?url=home");
//        System.out.println("Swagger2 generate RESTFul API html=http://localhost:8080/swagger-ui.html#/");
    }

}

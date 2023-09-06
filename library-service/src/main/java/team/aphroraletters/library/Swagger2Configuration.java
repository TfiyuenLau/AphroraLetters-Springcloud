package team.aphroraletters.library;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    @Bean(name = "swagger")
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")  //请求路径 http://localhost:8082/library/swagger-ui.html#/
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("team.aphroraletters.library"))  //作用范围,在那个包下
                .paths(PathSelectors.any())  //指定包下所有请求
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Aphrora Letters library RESTFul Api文档")
                .description("")
                .version("v0.1")
                .build();
    }

}

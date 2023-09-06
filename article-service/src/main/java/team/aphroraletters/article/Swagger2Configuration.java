package team.aphroraletters.article;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration//配置类
@EnableSwagger2
public class Swagger2Configuration {

    @Bean(name = "swagger")
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")  //请求路径 http://localhost:8081/article/swagger-ui.html#/
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("team.aphroraletters.article"))  //作用范围,在那个包下
                .paths(PathSelectors.any())  //指定包下所有请求
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Aphrora Letter's Article Module Api文档")  //标题
                .description("Aphrora Letter's Article Module Api文档......")  //描述
                .version("v0.1")  //版本
                .build();
    }

}

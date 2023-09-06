package team.aphroraletters.library;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
@MapperScan("team.aphroraletters.library.dao")
public class LibraryServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(LibraryServiceApplication.class, args);

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String property = environment.getProperty("spring.application.name");
        System.out.println("current spring.application.name=" + property);
        System.out.println("API document' address: http://localhost:8080/library/swagger-ui.html#/");
    }

}

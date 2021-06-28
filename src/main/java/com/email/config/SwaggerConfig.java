package com.email.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    public static final Contact CONTACT = new Contact("Martin Grellet", "http://fakeurl.com/",
            "martin.alejandro.grellet@gmail.com");

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.email"))
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo()).useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
       return new ApiInfo("swagger", "Swagger Documentation", "1.0", "urn:tos", CONTACT,
                "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList<>());
    }
}
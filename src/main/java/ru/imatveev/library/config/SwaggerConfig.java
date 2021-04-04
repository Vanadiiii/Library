package ru.imatveev.library.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Simple library")
                                .version("1.0")
                                .contact(new Contact().email("vanadiiii42@gmail.com").name("Ivan Matveev"))
                                .description("Just simple library for saving some books")
                );
    }
}

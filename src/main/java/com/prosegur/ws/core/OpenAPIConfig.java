package com.prosegur.ws.core;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * Clase se configuración para la documentación automatica de la API del componente.
 * Para consultar el API acceder a: host:port/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class OpenAPIConfig {

    /**
     * Get API doc info
     * @return Documentation doc
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.prosegur"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    /**
     * Generate API doc
     * @return API information
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Microservicio Java Example",
                "REST API de pruebas de Microservicio Java.",
                "v1",
                "Terms of service",
                new Contact("Prosegur", "", ""),
                "License of API", "API license URL", Collections.emptyList());
    }
}

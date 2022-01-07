package com.meicloud.apaas.preview.api.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author chenlei140
 * @className SwaggerConfig
 * @description SwaggerConfig
 * @date 2021/12/2 15:08
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String AUTHORIZATION = "Authorization";
    private static final String HEADER = "header";
    private static final String BASE_PACKAGE = "com.meicloud.apaas";

    @Value("${swagger.enable}")
    private final boolean enableSwagger = true;

    @Value("${spring.profiles.active}")
    private String env;

    @Bean
    public Docket createAPI() {
        return new Docket(DocumentationType.SWAGGER_2).enable(enableSwagger).forCodeGeneration(true).select()
            .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).paths(PathSelectors.any()).build()
            .pathMapping("/").apiInfo(apiInfo()).securitySchemes(Lists.newArrayList(apiKey()))
            .securityContexts(securityContexts());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("美云智数文档预览服务").version("1.0.0").build();
    }

    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION, AUTHORIZATION, HEADER);
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(SecurityContext.builder().securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex("^(?!auth).*$")).build());
        return securityContexts;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference(AUTHORIZATION, authorizationScopes));
        return securityReferences;
    }
}

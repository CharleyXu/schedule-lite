package com.xu.schedulelite.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author CharleyXu Created on 2019/3/19.
 */
@Configuration
@EnableSwagger2
@ConditionalOnExpression("'${swagger.enable}' == 'true'")
public class SwaggerConfig {

  @Bean
  public Docket apiSwagger2Service() {
    ApiInfo apiInfo = new ApiInfoBuilder()//
        .title("REST APIs")//
        .description("Schedule-lite Interfaces")//
        .version("1.0")//
        .build();
    return new Docket(DocumentationType.SWAGGER_2)//
        .groupName("rest-api")//
        .apiInfo(apiInfo)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.xu.schedulelite"))
        .paths(PathSelectors.ant("/**"))
        .build();
  }
}

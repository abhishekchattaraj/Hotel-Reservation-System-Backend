package com.cog.hotel.swagger;

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
 * Class for customizing swagger
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Hotel Reservation API Documentation")
				.description("This is the API documentation of a hotel reservation app").build();
	}

	/**
	 * Method for configuring swagger
	 * 
	 * @return This returns a Docket object with customized configuration
	 */
	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.cog.projectlogin")).paths(PathSelectors.any()).build()
				.apiInfo(apiInfo());
	}
}
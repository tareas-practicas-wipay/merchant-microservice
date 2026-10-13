package com.wipay.merchant.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Merchant Microservice", version = "1.0.0", description = "Merchant Microservice API"))
public class OpenApiConfig {
}

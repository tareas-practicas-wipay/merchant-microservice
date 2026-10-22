package com.wipay.merchant.config;

import com.wipay.merchant.interceptor.JwtInterceptor;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Merchant Microservice", version = "1.0.0", description = "Merchant Microservice API"))
public class OpenApiConfig {
    @Bean
    public OperationCustomizer tokenParameterCustomizer() {
        return (operation, handlerMethod) ->
            operation.addParametersItem(new QueryParameter()
                    .name(JwtInterceptor.TOKEN_PARAM)
                    .required(true)
                    .description("JWT con el nombre y la edad de quien hace la petición (generarlo con POST /tokens)")
                    .schema(new StringSchema()));
    }
}
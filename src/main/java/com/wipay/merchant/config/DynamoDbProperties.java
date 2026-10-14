package com.wipay.merchant.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "aws.dynamodb") //bean que agrupa en un único objeto las propiedades de configuración de DynamoDB
@Validated
public record DynamoDbProperties(
        @NotBlank
        String endpoint,
        @NotBlank
        String region,
        @NotBlank
        String tableName,
        @NotBlank
        String accessKey,
        @NotBlank
        String secretKey
) { }

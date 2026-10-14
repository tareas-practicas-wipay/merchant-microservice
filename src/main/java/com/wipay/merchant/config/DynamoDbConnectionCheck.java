package com.wipay.merchant.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@Component
@RequiredArgsConstructor
@Slf4j // Logger de Lombok para registrar mensajes de depuración y errores
public class DynamoDbConnectionCheck implements ApplicationRunner {

    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbProperties dynamoDbProperties;

    @Override
    public void run(@NonNull ApplicationArguments args) {
        try{

            var response = dynamoDbClient.describeTable(request -> request.tableName(dynamoDbProperties.tableName()));
            var status = response.table().tableStatus();

            log.info("Conectado a DynamoDB ({}): tabla {} en estado {}", dynamoDbProperties.endpoint(), dynamoDbProperties.tableName(), status);

        } catch (ResourceNotFoundException e) {
            String errorMessage = String.format("La tabla %s no existe. ¿Se ha ejecutado dynamodb-init?", dynamoDbProperties.tableName());
            throw new IllegalStateException(errorMessage, e);
        } catch (SdkClientException e) {
            String errorMessage = String.format("No se pudo conectar a DynamoDB (%s). ¿Está corriendo el contenedor de DynamoDB?", dynamoDbProperties.endpoint());
            throw new IllegalStateException(errorMessage, e);
        }
    }
}

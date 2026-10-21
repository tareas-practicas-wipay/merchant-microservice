package com.wipay.merchant.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta de GET /clients/{id}?simpleOutput=true de client-microservice.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientSimpleResponse {

    private String id;
}
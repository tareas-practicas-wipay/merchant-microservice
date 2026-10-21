package com.wipay.merchant.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Parámetros de la búsqueda de los merchants de un cliente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientIdSearchRequest {

    @NotBlank(message = "El id del cliente es obligatorio")
    private String clientId;
}
package com.wipay.merchant.model.request;

import com.wipay.merchant.model.enums.MerchantType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos que se pueden modificar de un merchant.
 * No incluye clientId: forma parte de la PK y no se puede cambiar.
 * --> NO SE PERMITE CAMBIAR EL CLIENTE DE UN MERCHANT
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantUpdateRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @NotNull(message = "El tipo de merchant es obligatorio")
    private MerchantType merchantType;
}
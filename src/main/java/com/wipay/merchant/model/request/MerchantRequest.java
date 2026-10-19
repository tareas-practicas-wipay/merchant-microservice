package com.wipay.merchant.model.request;

import com.wipay.merchant.model.enums.MerchantType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantRequest {
    @NotBlank(message = "El id del cliente es obligatorio")
    private String clientId;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @NotNull(message = "El tipo de merchant es obligatorio")
    private MerchantType merchantType;
}

package com.wipay.merchant.model;

import com.wipay.merchant.model.enums.MerchantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantModel {
    private String id;
    private String clientId;
    private String name;
    private String address;
    private MerchantType merchantType;
    private String status;
    private Instant createdDate;
}

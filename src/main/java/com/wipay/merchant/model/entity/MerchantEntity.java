package com.wipay.merchant.model.entity;

import com.wipay.dynamodb.entity.MainTable;
import com.wipay.merchant.model.enums.MerchantType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@NoArgsConstructor
@Getter
@Setter
public class MerchantEntity extends MainTable {

    private String name;
    private String address;
    private MerchantType merchantType;
}

package com.wipay.merchant.model.entity;

import com.wipay.dynamodb.entity.MainTable;
import com.wipay.merchant.model.enums.MerchantType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class MerchantEntity extends MainTable {

    public static final String CLIENT_PREFIX = "CLIENT#"; // Debe coincidir con ClientEntity.PREFIX
    public static final String PREFIX = "MERCHANT#";

    private String name;

    private String address;

    private MerchantType merchantType;

    private String keywordsSearch;

    public static String pkOf(String clientId) {
        return CLIENT_PREFIX + clientId;
    }

    public static String skOf(String id) {
        return PREFIX + id;
    }

    public static String gIndex2PkOf(String id) {
        return PREFIX + id;
    }

    public static String keywordsOf(String name) {
        return name == null ? null : name.trim().toLowerCase();
    }
}

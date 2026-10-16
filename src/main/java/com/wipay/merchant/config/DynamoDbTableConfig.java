package com.wipay.merchant.config;

import com.wipay.merchant.model.entity.MerchantEntity;
import com.wipay.dynamodb.autoconfigure.DynamoDbProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
public class DynamoDbTableConfig {

    @Bean
    public DynamoDbTable<MerchantEntity> merchantTable(DynamoDbEnhancedClient enhancedClient,
                                                     DynamoDbProperties properties) {
        return enhancedClient.table(properties.tableName(),
                                    TableSchema.fromBean(MerchantEntity.class));
    }
}

package com.wipay.merchant.repository;

import com.wipay.dynamodb.entity.MainTable;
import com.wipay.merchant.model.entity.MerchantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MerchantRepository {

    private final DynamoDbTable<MerchantEntity> merchantTable;

    public MerchantEntity save(MerchantEntity merchant) {
        merchantTable.putItem(merchant);
        return merchant;
    }

    public Optional<MerchantEntity> findById(String id) {
        QueryConditional condition = QueryConditional.keyEqualTo(
                key -> key.partitionValue(MerchantEntity.gIndex2PkOf(id)));

        return merchantTable.index(MainTable.GINDEX2)
                .query(condition)
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public List<MerchantEntity> findByClientId(String clientId) {
        QueryConditional condition = QueryConditional.sortBeginsWith(key ->
                                        key.partitionValue(MerchantEntity.pkOf(clientId))
                                    .sortValue(MerchantEntity.PREFIX));

        return merchantTable.query(condition)
                .items()
                .stream()
                .toList();
    }

    public List<MerchantEntity> findByName(String text) {
        QueryConditional condition = QueryConditional.keyEqualTo(
                key -> key.partitionValue(MerchantEntity.ENTITY_TYPE));

        Expression filter = Expression.builder()
                .expression("contains(#keywords, :text)")
                .putExpressionName("#keywords", "keywordsSearch")
                .putExpressionValue(":text", AttributeValue.fromS(MerchantEntity.keywordsOf(text)))
                .build();

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(condition)
                .filterExpression(filter)
                .build();

        return merchantTable.index(MainTable.GINDEX3)
                .query(request)
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }
}

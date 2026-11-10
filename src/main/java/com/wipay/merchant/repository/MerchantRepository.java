package com.wipay.merchant.repository;

import com.wipay.dynamodb.entity.MainTable;
import com.wipay.merchant.model.EntityStatus;
import com.wipay.merchant.model.entity.MerchantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MerchantRepository {

    /** Solo merchants activos: los borrados (INACTIVE) no aparecen en ninguna consulta. */
    private static final Expression ACTIVE_FILTER = Expression.builder()
            .expression("#status = :active")
            .putExpressionName("#status", "status")
            .putExpressionValue(":active", AttributeValue.fromS(EntityStatus.ACTIVE))
            .build();

    private final DynamoDbTable<MerchantEntity> merchantTable;

    public MerchantEntity save(MerchantEntity merchant) {
        merchantTable.putItem(merchant);
        return merchant;
    }

    public Optional<MerchantEntity> findById(String id) {
        QueryConditional condition = QueryConditional.keyEqualTo(
                key -> key.partitionValue(MerchantEntity.gIndex2PkOf(id)));

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(condition)
                .filterExpression(ACTIVE_FILTER)
                .build();

        return merchantTable.index(MainTable.GINDEX2)
                .query(request)
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public List<MerchantEntity> findByClientId(String clientId) {
        QueryConditional condition = QueryConditional.sortBeginsWith(key ->
                                        key.partitionValue(MerchantEntity.pkOf(clientId))
                                    .sortValue(MerchantEntity.PREFIX));

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(condition)
                .filterExpression(ACTIVE_FILTER)
                .build();

        return merchantTable.query(request)
                .items()
                .stream()
                .toList();
    }

    public List<MerchantEntity> findAll() {
        return queryMerchants(ACTIVE_FILTER);
    }

    public List<MerchantEntity> findByName(String text) {
        Expression nameFilter = Expression.builder()
                .expression("contains(#keywords, :text)")
                .putExpressionName("#keywords", "keywordsSearch")
                .putExpressionValue(":text", AttributeValue.fromS(MerchantEntity.keywordsOf(text)))
                .build();

        return queryMerchants(Expression.join(nameFilter, ACTIVE_FILTER, " AND "));
    }

    /** Query sobre gIndex3 (ENTITY#MERCHANT): lee solo merchants, no los clientes ni el resto de la tabla. */
    private List<MerchantEntity> queryMerchants(Expression filter) {
        QueryConditional condition = QueryConditional.keyEqualTo(
                key -> key.partitionValue(MerchantEntity.ENTITY_TYPE));

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

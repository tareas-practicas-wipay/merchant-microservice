package com.wipay.merchant.controller;

import com.wipay.merchant.mappers.MerchantMapper;
import com.wipay.merchant.model.MerchantModel;
import com.wipay.merchant.model.request.MerchantRequest;
import com.wipay.merchant.model.response.MerchantResponse;
import com.wipay.merchant.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.enhanced.dynamodb.MappedTableResource;

/**
 * ---------------------------------------------------------
 *                         (create)
 * [ CONTROLLER ] === mapper(Request->Model) ==> [ SERVICE ]
 * ---------------------------------------------------------
 * [ CONTROLLER ] <== mapper(Request<-Model) === [ SERVICE ]
 *                         (create)
 * ---------------------------------------------------------
 */

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Merchants", description = "Gestión de merchants")
public class MerchantController {

    private final MerchantService merchantService;
    private final MerchantMapper merchantMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear un merchant")
    public MerchantResponse create(MerchantRequest request){
        MerchantModel created = merchantService.create(merchantMapper.requestToModel(request));
        return merchantMapper.modelToResponse(created);
    }

}

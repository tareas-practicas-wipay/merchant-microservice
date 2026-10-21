package com.wipay.merchant.controller;

import com.wipay.merchant.mappers.MerchantMapper;
import com.wipay.merchant.model.MerchantModel;
import com.wipay.merchant.model.request.ClientIdSearchRequest;
import com.wipay.merchant.model.request.MerchantRequest;
import com.wipay.merchant.model.request.MerchantUpdateRequest;
import com.wipay.merchant.model.request.NameSearchRequest;
import com.wipay.merchant.model.response.MerchantResponse;
import com.wipay.merchant.model.response.SimpleResponse;
import com.wipay.merchant.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@RequestMapping("/merchants")
@RequiredArgsConstructor
@Tag(name = "Merchants", description = "Gestión de merchants")
public class MerchantController {

    private final MerchantService merchantService;
    private final MerchantMapper merchantMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear un merchant")
    public MerchantResponse create(@Valid @RequestBody MerchantRequest request){
        MerchantModel created = merchantService.create(merchantMapper.requestToModel(request));
        return merchantMapper.modelToResponse(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un merchant por su id", description = "Con simpleOutput=true devuelve solo el id")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(     // Como devuelve un Object, Swagger no sabe si está bien o no.
            oneOf = {MerchantResponse.class, SimpleResponse.class})))           // Con esto le decimos que si Object es una de las dos clases, devuelva 200 (OK)
    public Object findById(@PathVariable String id,
                           @RequestParam(defaultValue = "false") boolean simpleOutput) {
        MerchantModel merchant = merchantService.findById(id);

        return (simpleOutput)
                ? merchantMapper.modelToSimpleResponse(merchant)
                : merchantMapper.modelToResponse(merchant);
    }

    @GetMapping("/search/by-name")
    @Operation(summary = "Buscar merchants por nombre",
                description = "Coincidencia parcial sin distinguir mayúsculas ('pay' encuentra 'Wipay')")
    public List<MerchantResponse> findByName(@Valid @ParameterObject NameSearchRequest name) {
        return merchantMapper.modelsToResponses(merchantService.findByName(name.getName()));
    }

    @GetMapping("/search/by-client")
    @Operation(summary = "Obtener los merchants de un cliente")
    public List<MerchantResponse> findByClientId(@Valid @ParameterObject ClientIdSearchRequest request) {
        return merchantMapper.modelsToResponses(merchantService.findByClientId(request.getClientId()));
    }

    @GetMapping("/{id}/client")
    @Operation(summary = "Obtener el cliente al que pertenece un merchant",
            description = "Devuelve el id del cliente")
    public SimpleResponse findClientOfMerchant(@PathVariable String id) {
        return merchantMapper.modelToClientReference(merchantService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un merchant")
    public MerchantResponse update(@PathVariable String id, @Valid @RequestBody MerchantUpdateRequest updateRequest) {
        MerchantModel updated = merchantService.update(id, merchantMapper.updateRequestToModel(updateRequest));
        return merchantMapper.modelToResponse(updated);
    }
}
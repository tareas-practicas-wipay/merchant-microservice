package com.wipay.merchant.mappers;

import com.wipay.merchant.model.MerchantModel;
import com.wipay.merchant.model.entity.MerchantEntity;
import com.wipay.merchant.model.request.MerchantRequest;
import com.wipay.merchant.model.response.MerchantResponse;
import com.wipay.merchant.model.response.SimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/** Este mapper deberá realizar las siguientes conversiones:
 * ----------------------------------------------------------------------------------------
 *                         (crear)                          (guardar)
 * [ CONTROLADOR ] === Request->Model ==> [ SERVICIO ] === Model->Entity ==> [ REPOSITORIO ]
 * ----------------------------------------------------------------------------------------
 * [ CONTROLADOR ] <= Response<-Model === [ SERVICIO ] <== Model<-Entity === [ REPOSITORIO ]
 *                <=SimpleResponse<-Model=                   (leer)
 *               (devolver completo/simple)
 * ----------------------------------------------------------------------------------------
 */

// componentModel = "spring -> @Component
// unmappedTargetPolicy = ReportingPolicy.ERROR -> Si un campo se queda sin recibir ningún valor, no solo da un warning, la compilación falla
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MerchantMapper {

    /// ENTRADA -> MODELO GENÉRICO (controlador --creación-> servicio)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdDate", ignore = true) // id, status y createdDate los pone el servicio
    MerchantModel requestToModel(MerchantRequest request);

    /// MODELO GENÉRICO -> ENTIDAD (servicio --guardado-> repositorio)
    @Mapping(target = "pk", expression = "java(MerchantEntity.pkOf(model.getClientId()))")
    @Mapping(target = "sk", expression = "java(MerchantEntity.skOf(model.getId()))")
    @Mapping(target = "GIndex2Pk", expression = "java(MerchantEntity.gIndex2PkOf(model.getId()))")
    @Mapping(target = "GIndex3Pk", constant = MerchantEntity.ENTITY_TYPE)
    @Mapping(target = "keywordsSearch", expression = "java(MerchantEntity.keywordsOf(model.getName()))")
    MerchantEntity modelToEntity(MerchantModel model);

    /// ENTIDAD -> MODELO GENÉRICO (repositorio --lectura-> servicio)
    @Mapping(target = "clientId", expression = "java(MerchantEntity.clientIdOf(entity.getPk()))")
    MerchantModel entityToModel(MerchantEntity entity);

    List<MerchantModel> entitiesToModels(List<MerchantEntity> entities);

    /// MODELO GENÉRICO -> SALIDA (servicio --devolver-completo-> controlador)
    MerchantResponse modelToResponse(MerchantModel model);
    List<MerchantResponse> modelsToResponses(List<MerchantModel> models);

    /// MODELO GENÉRICO -> SALIDA SIMPLE (servicio --devolver-simple-> controlador)
    SimpleResponse modelToSimpleResponse(MerchantModel model);

    /// MODELO MERCHANT GENÉRICO -> SALIDA SIMPLE CON id CLIENTID
    @Mapping(target = "id", source = "clientId")
    SimpleResponse modelToClientReference(MerchantModel model);
}

package com.wipay.merchant.service.impl;

import com.wipay.merchant.exception.NotFoundException;
import com.wipay.merchant.mappers.MerchantMapper;
import com.wipay.merchant.model.MerchantModel;
import com.wipay.merchant.model.entity.MerchantEntity;
import com.wipay.merchant.repository.MerchantRepository;
import com.wipay.merchant.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    private static final String STATUS_ACTIVE = "ACTIVE";

    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

    @Override
    public MerchantModel create(MerchantModel merchant) {
        // TODO (Pendiente tarea 3): comprobar con Feign que el cliente existe
        merchant.setId(UUID.randomUUID().toString());
        merchant.setStatus(STATUS_ACTIVE);
        merchant.setCreatedDate(Instant.now());

        MerchantEntity saved = merchantRepository.save(merchantMapper.modelToEntity(merchant));
        return merchantMapper.entityToModel(saved);
    }

    @Override
    public MerchantModel findById(String id) {
        return merchantRepository.findById(id)
                .map(merchantMapper::entityToModel)
                .orElseThrow(() -> new NotFoundException("No existe ningún merchant con id " + id));
    }

    @Override
    public List<MerchantModel> findByName(String name) {
        return merchantMapper.entitiesToModels(merchantRepository.findByName(name));
    }

    @Override
    public List<MerchantModel> findByClientId(String clientId) {
        return merchantMapper.entitiesToModels(merchantRepository.findByClientId(clientId));
    }
}

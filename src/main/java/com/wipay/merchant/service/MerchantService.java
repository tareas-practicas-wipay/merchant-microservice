package com.wipay.merchant.service;

import com.wipay.merchant.model.MerchantModel;

import java.util.List;

public interface MerchantService {

    MerchantModel create(MerchantModel merchant);

    MerchantModel findById(String id);

    List<MerchantModel> findByName(String name);

    List<MerchantModel> findByClientId(String clientId);

    MerchantModel update(String id, MerchantModel changes);
}

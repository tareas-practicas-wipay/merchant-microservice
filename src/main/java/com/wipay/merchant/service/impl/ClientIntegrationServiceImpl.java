package com.wipay.merchant.service.impl;

import com.wipay.merchant.exception.ServiceUnavailableException;
import com.wipay.merchant.integration.ClientFeignClient;
import com.wipay.merchant.service.ClientIntegrationService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientIntegrationServiceImpl implements ClientIntegrationService {

    private final ClientFeignClient clientFeignClient;

    @Override
    public boolean clientExists(String clientId) {
        try {
            clientFeignClient.findById(clientId, true);
            return true;
        } catch (FeignException.NotFound ex) {
            return false;
        } catch (FeignException ex) {
            throw new ServiceUnavailableException(
                    "No se ha podido consultar client-microservice para el cliente " + clientId, ex);
        }
    }
}
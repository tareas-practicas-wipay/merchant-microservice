package com.wipay.merchant.service.impl;

import com.wipay.merchant.config.ResilienceConfig;
import com.wipay.merchant.exception.ServiceUnavailableException;
import com.wipay.merchant.integration.ClientFeignClient;
import com.wipay.merchant.service.ClientIntegrationService;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientIntegrationServiceImpl implements ClientIntegrationService {

    private final ClientFeignClient clientFeignClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Override
    public boolean clientExists(String clientId) {
        return circuitBreakerFactory.create(ResilienceConfig.CLIENT_SERVICE)
                .run(() -> callClientService(clientId),
                        error -> {
                            throw unavailable(clientId, error);
                        });
    }

    /** El 404 se resuelve aquí dentro: para el circuit breaker es una llamada correcta. */
    private boolean callClientService(String clientId) {
        try {
            clientFeignClient.findById(clientId, true);
            return true;
        } catch (FeignException.NotFound ex) {
            return false;
        }
    }

    private ServiceUnavailableException unavailable(String clientId, Throwable error) {
        String reason = error instanceof CallNotPermittedException
                ? "circuito abierto: client-microservice ha fallado varias veces seguidas"
                : "no ha respondido correctamente";
        return new ServiceUnavailableException(
                "No se ha podido consultar el cliente " + clientId + " (" + reason + ")", error);
    }
}
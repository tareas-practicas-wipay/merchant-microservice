package com.wipay.merchant.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Cliente HTTP de client-microservice. Feign genera la implementación al arrancar.
 */
@FeignClient(name = "client-service", url = "${client.service.url}")
public interface ClientFeignClient {

    @GetMapping("/clients/{id}")
    ClientSimpleResponse findById(@PathVariable("id") String id,
                                  @RequestParam("simpleOutput") boolean simpleOutput);
}
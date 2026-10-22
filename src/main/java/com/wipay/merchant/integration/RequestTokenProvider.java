package com.wipay.merchant.integration;

import com.wipay.merchant.interceptor.JwtInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Obtiene el JWT de la petición en curso para reenviarlo a otros microservicios.
 * Debe llamarse desde el hilo de la petición, antes de entrar en el circuit breaker.
 */
@Component
public class RequestTokenProvider {

    public String currentToken() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            HttpServletRequest request = attributes.getRequest();
            return request.getParameter(JwtInterceptor.TOKEN_PARAM);
        }
        throw new IllegalStateException("No hay ninguna petición HTTP en curso de la que obtener el token");
    }
}
package com.wipay.merchant.service.impl;

import com.wipay.merchant.config.JwtProperties;
import com.wipay.merchant.exception.InvalidTokenException;
import com.wipay.merchant.model.TokenUser;
import com.wipay.merchant.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private static final String CLAIM_NAME = "name";
    private static final String CLAIM_AGE = "age";

    private final SecretKey jwtSigningKey;
    private final JwtProperties jwtProperties;

    @Override
    public TokenUser parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(jwtSigningKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String name = claims.get(CLAIM_NAME, String.class);
            Integer age = claims.get(CLAIM_AGE, Integer.class);
            if (name == null || age == null) {
                throw new InvalidTokenException("El token no contiene el nombre o la edad");
            }
            return new TokenUser(name, age);

        } catch (JwtException | IllegalArgumentException ex) {
            throw new InvalidTokenException("El token no es válido o ha caducado", ex);
        }
    }
}
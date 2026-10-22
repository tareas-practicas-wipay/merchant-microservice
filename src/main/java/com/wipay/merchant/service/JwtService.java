package com.wipay.merchant.service;

import com.wipay.merchant.model.TokenUser;

public interface JwtService {

    TokenUser parseToken(String token);
}
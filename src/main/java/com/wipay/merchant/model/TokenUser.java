package com.wipay.merchant.model;

import lombok.Value;

/**
 * Persona que envía la petición, según los datos de su JWT.
 */
@Value
public class TokenUser {

    String name;
    int age;
}
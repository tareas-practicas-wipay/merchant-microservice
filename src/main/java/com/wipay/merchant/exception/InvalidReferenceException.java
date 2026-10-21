package com.wipay.merchant.exception;

/**
 * La petición hace referencia a un recurso que no existe (por ejemplo, un clientId).
 */
public class InvalidReferenceException extends RuntimeException {

    public InvalidReferenceException(String message) {
        super(message);
    }
}
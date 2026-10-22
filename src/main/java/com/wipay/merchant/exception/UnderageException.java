package com.wipay.merchant.exception;

/**
 * La persona del token es menor de edad y no puede acceder.
 */
public class UnderageException extends RuntimeException {

    public UnderageException(String message) {
        super(message);
    }
}
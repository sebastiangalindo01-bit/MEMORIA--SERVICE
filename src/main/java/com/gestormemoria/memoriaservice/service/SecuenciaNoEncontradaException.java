package com.gestormemoria.memoriaservice.service;

public class SecuenciaNoEncontradaException extends RuntimeException {

    public SecuenciaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}

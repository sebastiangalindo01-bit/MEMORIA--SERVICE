package com.gestormemoria.memoriaservice.exception;

public class SecuenciaNoEncontradaException extends RuntimeException {
    public SecuenciaNoEncontradaException(Long id) {
        super("No existe una secuencia con id " + id + ".");
    }
}

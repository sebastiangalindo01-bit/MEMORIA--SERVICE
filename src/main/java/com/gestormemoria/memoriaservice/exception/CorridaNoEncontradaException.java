package com.gestormemoria.memoriaservice.exception;

public class CorridaNoEncontradaException extends RuntimeException {
    public CorridaNoEncontradaException(Long id) { super("No existe una corrida con id " + id + "."); }
}

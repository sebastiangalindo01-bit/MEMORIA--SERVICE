package com.gestormemoria.memoriaservice.exception;

public class MapaNoEncontradoException extends RuntimeException {
    public MapaNoEncontradoException(Long id) { super("No existe un mapa con id " + id + "."); }
}

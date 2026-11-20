package com.gestormemoria.memoriaservice.mapa;

public class MapaNoEncontradoException extends RuntimeException {
    public MapaNoEncontradoException(Long id) {
        super("No existe un mapa con id " + id + ".");
    }
}
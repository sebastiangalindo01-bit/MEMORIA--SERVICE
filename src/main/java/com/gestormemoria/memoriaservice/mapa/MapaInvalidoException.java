package com.gestormemoria.memoriaservice.mapa;

public class MapaInvalidoException extends RuntimeException {
    public MapaInvalidoException(String mensaje) {
        super(mensaje);
    }
}
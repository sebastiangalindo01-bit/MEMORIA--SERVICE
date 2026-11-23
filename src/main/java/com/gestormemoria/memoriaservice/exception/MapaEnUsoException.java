package com.gestormemoria.memoriaservice.exception;

public class MapaEnUsoException extends RuntimeException {
    public MapaEnUsoException(Long id) {
        super("No se puede eliminar el mapa " + id + " porque ya forma parte de una corrida guardada.");
    }
}

package com.gestormemoria.memoriaservice.mapa;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/** Errores propios de los mapas. Complementa el ManejadorErrores general. */
@RestControllerAdvice
public class MapaErrores {

    @ExceptionHandler(MapaInvalidoException.class)
    public ResponseEntity<Map<String, String>> invalido(MapaInvalidoException ex) {
        return ResponseEntity.badRequest().body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(MapaNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(MapaNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", ex.getMessage()));
    }
}
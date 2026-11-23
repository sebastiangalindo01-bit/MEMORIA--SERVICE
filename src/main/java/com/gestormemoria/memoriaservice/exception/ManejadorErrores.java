package com.gestormemoria.memoriaservice.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

/** Mensajes en lenguaje de persona: nunca una traza ni un código crudo (espíritu de RQ-01). */
@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(e -> e.getDefaultMessage())
                .orElse("La solicitud no es válida.");
        return ResponseEntity.badRequest().body(Map.of("mensaje", mensaje));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> cuerpoIlegible(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of("mensaje",
                "No pude leer la secuencia. Revisa que el tipo sea ASIGNAR o LIBERAR y que los números sean enteros."));
    }

    @ExceptionHandler(SecuenciaInvalidaException.class)
    public ResponseEntity<Map<String, String>> invalida(SecuenciaInvalidaException ex) {
        return ResponseEntity.badRequest().body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(SecuenciaNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> noEncontrada(SecuenciaNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler({MapaNoEncontradoException.class, CorridaNoEncontradaException.class})
    public ResponseEntity<Map<String, String>> recursoNoEncontrado(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(MapaEnUsoException.class)
    public ResponseEntity<Map<String, String>> mapaEnUso(MapaEnUsoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Map<String, String>> motorNoDisponible(RestClientException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("mensaje",
                "El motor de asignación no responde ahora. La corrida no se guardó; vuelve a intentarlo."));
    }
}

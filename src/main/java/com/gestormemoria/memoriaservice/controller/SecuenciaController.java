package com.gestormemoria.memoriaservice.controller;

import com.gestormemoria.memoriaservice.dto.CrearSecuenciaRequest;
import com.gestormemoria.memoriaservice.dto.SecuenciaResponse;
import com.gestormemoria.memoriaservice.service.SecuenciaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secuencias")
public class SecuenciaController {

    private final SecuenciaService secuenciaService;

    public SecuenciaController(SecuenciaService secuenciaService) {
        this.secuenciaService = secuenciaService;
    }

    @PostMapping
    public ResponseEntity<SecuenciaResponse> crearSecuencia(
            @RequestBody CrearSecuenciaRequest request
    ) {
        SecuenciaResponse respuesta =
                secuenciaService.crearSecuencia(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecuenciaResponse> obtenerSecuencia(
            @PathVariable Long id
    ) {
        SecuenciaResponse respuesta =
                secuenciaService.obtenerSecuencia(id);

        return ResponseEntity.ok(respuesta);
    }
}

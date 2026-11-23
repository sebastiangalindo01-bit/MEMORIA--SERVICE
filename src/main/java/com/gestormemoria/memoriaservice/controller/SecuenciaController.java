package com.gestormemoria.memoriaservice.controller;

import com.gestormemoria.memoriaservice.dto.SecuenciaRequest;
import com.gestormemoria.memoriaservice.dto.SecuenciaResponse;
import com.gestormemoria.memoriaservice.service.SecuenciaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secuencias")
public class SecuenciaController {

    private final SecuenciaService servicio;

    public SecuenciaController(SecuenciaService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<SecuenciaResponse> crear(@Valid @RequestBody SecuenciaRequest req) {
        SecuenciaResponse creada = servicio.crear(req);
        return ResponseEntity.created(URI.create("/secuencias/" + creada.id())).body(creada);
    }

    @GetMapping("/{id}")
    public SecuenciaResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @GetMapping
    public List<SecuenciaResponse> listar() {
        return servicio.listar();
    }
}

package com.gestormemoria.memoriaservice.controller;

import com.gestormemoria.memoriaservice.dto.CorridaRequest;
import com.gestormemoria.memoriaservice.service.CorridaService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/corridas")
public class CorridaController {
    private final CorridaService servicio;
    public CorridaController(CorridaService servicio) { this.servicio = servicio; }
    @PostMapping public Object ejecutar(@Valid @RequestBody CorridaRequest request, Authentication auth) { return servicio.ejecutar(request, auth); }
    @GetMapping("/{id}") public Object obtener(@PathVariable Long id, Authentication auth) { return servicio.obtener(id, auth); }
    @GetMapping public Object historial(Authentication auth) { return servicio.historial(auth); }
}

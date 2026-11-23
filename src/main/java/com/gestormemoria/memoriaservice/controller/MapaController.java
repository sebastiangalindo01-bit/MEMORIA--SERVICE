package com.gestormemoria.memoriaservice.controller;

import com.gestormemoria.memoriaservice.dto.MapaRequest;
import com.gestormemoria.memoriaservice.dto.MapaResponse;
import com.gestormemoria.memoriaservice.service.MapaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mapas")
public class MapaController {
    private final MapaService servicio;
    public MapaController(MapaService servicio) { this.servicio = servicio; }
    @PostMapping
    public ResponseEntity<MapaResponse> crear(@Valid @RequestBody MapaRequest request) {
        var mapa = servicio.crear(request);
        return ResponseEntity.created(URI.create("/mapas/" + mapa.id())).body(mapa);
    }
    @GetMapping public List<MapaResponse> listar() { return servicio.listar(); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

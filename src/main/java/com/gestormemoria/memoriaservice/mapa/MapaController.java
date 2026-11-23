package com.gestormemoria.memoriaservice.mapa;

import com.gestormemoria.memoriaservice.mapa.dto.MapaRequest;
import com.gestormemoria.memoriaservice.mapa.dto.MapaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/mapas")
@RequiredArgsConstructor
public class MapaController {

    private final MapaService service;

    @PostMapping
    public ResponseEntity<MapaResponse> crear(@Valid @RequestBody MapaRequest req) {
        MapaResponse creado = service.crear(req);
        return ResponseEntity.created(URI.create("/mapas/" + creado.id())).body(creado);
    }

    @GetMapping
    public List<MapaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public MapaResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }
}

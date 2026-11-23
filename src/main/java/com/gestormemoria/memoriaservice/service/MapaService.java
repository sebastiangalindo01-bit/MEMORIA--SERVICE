package com.gestormemoria.memoriaservice.service;

import com.gestormemoria.memoriaservice.dto.MapaRequest;
import com.gestormemoria.memoriaservice.dto.MapaResponse;
import com.gestormemoria.memoriaservice.exception.SecuenciaInvalidaException;
import com.gestormemoria.memoriaservice.exception.MapaNoEncontradoException;
import com.gestormemoria.memoriaservice.exception.MapaEnUsoException;
import com.gestormemoria.memoriaservice.model.BloqueMapa;
import com.gestormemoria.memoriaservice.model.MapaMemoria;
import com.gestormemoria.memoriaservice.repository.MapaRepository;
import com.gestormemoria.memoriaservice.repository.CorridaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MapaService {
    private final MapaRepository mapas;
    private final CorridaRepository corridas;
    public MapaService(MapaRepository mapas, CorridaRepository corridas) { this.mapas = mapas; this.corridas = corridas; }

    @Transactional
    public MapaResponse crear(MapaRequest request) {
        long esperado = request.bloques().get(0).inicio();
        for (int i = 0; i < request.bloques().size(); i++) {
            var bloque = request.bloques().get(i);
            if (bloque.tamano() <= 0 || bloque.inicio() != esperado)
                throw new SecuenciaInvalidaException("Los bloques deben ser contiguos, estar ordenados y tener tamaño positivo.");
            esperado += bloque.tamano();
            if (esperado > Integer.MAX_VALUE) throw new SecuenciaInvalidaException("El mapa supera el rango de direcciones permitido.");
        }
        MapaMemoria mapa = new MapaMemoria(request.nombre().trim());
        for (int i = 0; i < request.bloques().size(); i++) {
            var bloque = request.bloques().get(i);
            mapa.agregar(new BloqueMapa(i + 1, bloque.inicio(), bloque.tamano()));
        }
        return MapaResponse.de(mapas.save(mapa));
    }

    @Transactional(readOnly = true)
    public List<MapaResponse> listar() { return mapas.findAll().stream().map(MapaResponse::de).toList(); }

    @Transactional(readOnly = true)
    public MapaMemoria exigir(Long id) {
        return mapas.findById(id).orElseThrow(() -> new MapaNoEncontradoException(id));
    }

    @Transactional
    public void eliminar(Long id) {
        MapaMemoria mapa = exigir(id);
        if (corridas.existsByMapa_Id(id)) throw new MapaEnUsoException(id);
        mapas.delete(mapa);
    }
}

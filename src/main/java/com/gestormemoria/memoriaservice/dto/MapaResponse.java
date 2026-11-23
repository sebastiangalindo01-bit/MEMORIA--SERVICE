package com.gestormemoria.memoriaservice.dto;

import com.gestormemoria.memoriaservice.model.MapaMemoria;
import java.util.List;

public record MapaResponse(Long id, String nombre, List<Bloque> bloques) {
    public record Bloque(int inicio, int tamano) { }
    public static MapaResponse de(MapaMemoria mapa) {
        return new MapaResponse(mapa.getId(), mapa.getNombre(), mapa.getBloques().stream()
                .map(b -> new Bloque(b.getInicio(), b.getTamano())).toList());
    }
}

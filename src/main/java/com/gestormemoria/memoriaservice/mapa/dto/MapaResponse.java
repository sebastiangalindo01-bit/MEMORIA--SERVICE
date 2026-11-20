package com.gestormemoria.memoriaservice.mapa.dto;

import com.gestormemoria.memoriaservice.mapa.MapaMemoria;

import java.util.List;

public record MapaResponse(Long id, String nombre, List<BloqueResponse> bloques) {

    public static MapaResponse de(MapaMemoria mapa) {
        return new MapaResponse(
                mapa.getId(),
                mapa.getNombre(),
                mapa.getBloques().stream()
                        .map(b -> new BloqueResponse(b.getInicio(), b.getTamanoKb()))
                        .toList());
    }
}

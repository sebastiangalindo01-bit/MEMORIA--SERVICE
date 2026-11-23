package com.gestormemoria.memoriaservice.dto;

import com.gestormemoria.memoriaservice.entity.TipoOperacion;

public record OperacionResponse(
        Integer posicion,
        TipoOperacion tipo,
        String procesoId,
        Integer tamanoKb
) {
}

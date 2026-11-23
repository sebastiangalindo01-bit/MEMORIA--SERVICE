package com.gestormemoria.memoriaservice.dto;

import com.gestormemoria.memoriaservice.entity.TipoOperacion;

public record OperacionRequest(
        TipoOperacion tipo,
        String procesoId,
        Integer tamanoKb
) {
}

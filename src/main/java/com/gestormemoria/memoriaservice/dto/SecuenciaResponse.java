package com.gestormemoria.memoriaservice.dto;

import java.util.List;

public record SecuenciaResponse(
        Long id,
        String nombre,
        List<OperacionResponse> operaciones
) {
}

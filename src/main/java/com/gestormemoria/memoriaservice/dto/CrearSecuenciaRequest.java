package com.gestormemoria.memoriaservice.dto;

import java.util.List;

public record CrearSecuenciaRequest(
        String nombre,
        List<OperacionRequest> operaciones
) {
}

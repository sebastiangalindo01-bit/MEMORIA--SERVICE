package com.gestormemoria.memoriaservice.dto;

import com.gestormemoria.memoriaservice.model.Secuencia;
import java.util.List;

public record SecuenciaResponse(Long id, String nombre, Long semilla, int total, List<OperacionResponse> operaciones) {
    public static SecuenciaResponse de(Secuencia s) {
        List<OperacionResponse> ops = s.getOperaciones().stream().map(OperacionResponse::de).toList();
        return new SecuenciaResponse(s.getId(), s.getNombre(), s.getSemilla(), ops.size(), ops);
    }
}

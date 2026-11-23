package com.gestormemoria.memoriaservice.dto;

import com.gestormemoria.memoriaservice.model.Operacion;
import com.gestormemoria.memoriaservice.model.TipoOperacion;

public record OperacionResponse(int orden, TipoOperacion tipo, int proceso, Integer tamano) {
    public static OperacionResponse de(Operacion op) {
        return new OperacionResponse(op.getOrden(), op.getTipo(), op.getProceso(), op.getTamano());
    }
}

package com.gestormemoria.memoriaservice.dto;

import com.gestormemoria.memoriaservice.model.TipoOperacion;
import jakarta.validation.constraints.NotNull;

public record OperacionRequest(
        @NotNull(message = "Cada operación debe indicar su tipo: ASIGNAR o LIBERAR.")
        TipoOperacion tipo,

        @NotNull(message = "Cada operación debe indicar el proceso.")
        Integer proceso,

        /** Obligatorio y mayor que 0 solo si el tipo es ASIGNAR (se valida en el servicio). */
        Integer tamano) {
}

package com.gestormemoria.memoriaservice.dto;

import com.gestormemoria.memoriaservice.model.TipoOperacion;
import jakarta.validation.constraints.NotNull;

public record OperacionRequest(
        @NotNull TipoOperacion tipo,
        @NotNull Integer proceso,
        Integer tamano) { }

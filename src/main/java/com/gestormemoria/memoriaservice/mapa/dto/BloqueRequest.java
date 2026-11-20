package com.gestormemoria.memoriaservice.mapa.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record BloqueRequest(
        @NotNull(message = "la dirección de inicio es obligatoria")
        @PositiveOrZero(message = "la dirección de inicio no puede ser negativa")
        Integer inicio,

        @NotNull(message = "el tamaño en KB es obligatorio")
        @Positive(message = "el tamaño debe ser mayor que cero")
        Integer tamanoKb) {
}

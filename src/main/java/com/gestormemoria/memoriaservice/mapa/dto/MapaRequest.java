package com.gestormemoria.memoriaservice.mapa.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record MapaRequest(
        @NotBlank(message = "el nombre del mapa es obligatorio")
        @Size(max = 100, message = "el nombre no puede superar 100 caracteres")
        String nombre,

        @NotEmpty(message = "el mapa debe tener al menos un bloque")
        @Valid
        List<BloqueRequest> bloques) {
}

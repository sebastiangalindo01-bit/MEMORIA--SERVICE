package com.gestormemoria.memoriaservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record SecuenciaRequest(
        @NotBlank(message = "La secuencia necesita un nombre.")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres.")
        String nombre,

        Long semilla,

        @NotEmpty(message = "La secuencia debe tener al menos una operación.")
        List<@Valid OperacionRequest> operaciones) {
}

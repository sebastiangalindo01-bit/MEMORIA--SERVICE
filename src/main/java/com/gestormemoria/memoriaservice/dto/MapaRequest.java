package com.gestormemoria.memoriaservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record MapaRequest(@NotBlank @Size(max = 100) String nombre,
                          @NotEmpty List<@Valid BloqueRequest> bloques) {
    public record BloqueRequest(@Min(0) int inicio, @Min(1) int tamano) { }
}

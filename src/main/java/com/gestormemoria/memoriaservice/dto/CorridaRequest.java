package com.gestormemoria.memoriaservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.util.List;

public record CorridaRequest(@NotNull Long mapaId, @NotNull Long secuenciaId,
                             @NotEmpty List<@NotNull @Min(0) Integer> estrategias) { }

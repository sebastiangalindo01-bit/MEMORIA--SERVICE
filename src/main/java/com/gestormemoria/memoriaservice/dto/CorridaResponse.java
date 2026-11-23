package com.gestormemoria.memoriaservice.dto;

import tools.jackson.databind.JsonNode;
import java.time.Instant;

public record CorridaResponse(Long id, Long mapaId, Long secuenciaId, String estrategia,
                              String propietario, Instant creadaEn, JsonNode resultado) { }

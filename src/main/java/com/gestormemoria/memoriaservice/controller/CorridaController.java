package com.gestormemoria.memoriaservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestormemoria.memoriaservice.client.AsignadorClient;

@RestController
@RequestMapping("/corridas")
public class CorridaController {

    @Autowired
    private AsignadorClient asignadorClient;

    @PostMapping("/preparar")
    public ResponseEntity<?> prepararCorrida(@RequestBody Map<String, Object> payload) {
        // Criterio de aceptación 5: Validar que se envíen los 3 parámetros requeridos[cite: 5]
        if (!payload.containsKey("mapaId") || payload.get("mapaId") == null) {
            return ResponseEntity.badRequest().body("Error: Debe seleccionar un mapa de memoria");
        }
        if (!payload.containsKey("secuenciaId") || payload.get("secuenciaId") == null) {
            return ResponseEntity.badRequest().body("Error: Debe seleccionar una secuencia de solicitudes");
        }
        if (!payload.containsKey("estrategia") || payload.get("estrategia") == null) {
            return ResponseEntity.badRequest().body("Error: Debe seleccionar una estrategia (FIRST_FIT, BEST_FIT, WORST_FIT)");
        }

        Long mapaId = Long.valueOf(payload.get("mapaId").toString());
        Long secuenciaId = Long.valueOf(payload.get("secuenciaId").toString());
        String estrategia = payload.get("estrategia").toString();

        Map<String, Object> respuesta = asignadorClient.enviarSimulacion(mapaId, secuenciaId, estrategia);
        return ResponseEntity.ok(respuesta);
    }
}
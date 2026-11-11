package com.gestormemoria.memoriaservice.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AsignadorClient {

    private final RestTemplate restTemplate;

    @Value("${asignador.service.url:http://localhost:8081}")
    private String asignadorServiceUrl;

    public AsignadorClient() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> enviarSimulacion(Long mapaId, Long secuenciaId, String estrategia) {
        int codigoEstrategia = traducirEstrategia(estrategia);

        // Arreglos planos requeridos por el contrato nativo / asignador[cite: 18]
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("mapaId", mapaId);
        requestBody.put("secuenciaId", secuenciaId);
        requestBody.put("bloqueInicio", new int[]{0, 100, 250});
        requestBody.put("bloqueTamano", new int[]{100, 150, 200});
        requestBody.put("nBloques", 3);
        requestBody.put("opTipo", new int[]{0, 0});
        requestBody.put("opProceso", new int[]{1, 2});
        requestBody.put("opTamano", new int[]{50, 80});
        requestBody.put("nOps", 2);
        requestBody.put("estrategia", codigoEstrategia);

        String url = asignadorServiceUrl + "/simular";
        return restTemplate.postForObject(url, requestBody, Map.class);
    }

    private int traducirEstrategia(String estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia no puede ser nula");
        }
        switch (estrategia.toUpperCase()) {
            case "FIRST_FIT": return 0;
            case "BEST_FIT": return 1;
            case "WORST_FIT": return 2;
            default: throw new IllegalArgumentException("Estrategia no válida: " + estrategia);
        }
    }
}
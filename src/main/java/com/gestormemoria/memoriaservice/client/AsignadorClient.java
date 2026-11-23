package com.gestormemoria.memoriaservice.client;

import com.gestormemoria.memoriaservice.model.MapaMemoria;
import com.gestormemoria.memoriaservice.model.Secuencia;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AsignadorClient {
    private final RestClient http;

    public AsignadorClient(@Value("${asignador.service.url:http://localhost:8080}") String baseUrl) {
        this.http = RestClient.builder().baseUrl(baseUrl).build();
    }

    public Map<String, Object> simular(MapaMemoria mapa, Secuencia secuencia, int estrategia) {
        int nBloques = mapa.getBloques().size();
        int nOps = secuencia.getOperaciones().size();
        int[] inicios = new int[nBloques], tamanos = new int[nBloques];
        int[] tipos = new int[nOps], procesos = new int[nOps], solicitudes = new int[nOps];
        for (int i = 0; i < nBloques; i++) { inicios[i] = mapa.getBloques().get(i).getInicio(); tamanos[i] = mapa.getBloques().get(i).getTamano(); }
        for (int i = 0; i < nOps; i++) {
            var op = secuencia.getOperaciones().get(i);
            tipos[i] = op.getTipo().getCodigo(); procesos[i] = op.getProceso(); solicitudes[i] = op.getTamano() == null ? 0 : op.getTamano();
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("mapaId", mapa.getId()); body.put("secuenciaId", secuencia.getId());
        body.put("bloqueInicio", inicios); body.put("bloqueTamano", tamanos); body.put("nBloques", nBloques);
        body.put("opTipo", tipos); body.put("opProceso", procesos); body.put("opTamano", solicitudes); body.put("nOps", nOps); body.put("estrategia", estrategia);
        var auth = SecurityContextHolder.getContext().getAuthentication();
        RestClient.RequestBodySpec request = http.post().uri("/simular").contentType(org.springframework.http.MediaType.APPLICATION_JSON);
        if (auth instanceof JwtAuthenticationToken jwt) request.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getToken().getTokenValue());
        Map<?, ?> response = request.body(body).retrieve().body(Map.class);
        if (response == null) throw new IllegalStateException("El asignador no devolvió un resultado.");
        Map<String, Object> result = new LinkedHashMap<>(); response.forEach((k, v) -> result.put(String.valueOf(k), v));
        return result;
    }
}

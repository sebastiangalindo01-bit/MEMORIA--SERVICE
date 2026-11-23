package com.gestormemoria.memoriaservice.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.gestormemoria.memoriaservice.client.AsignadorClient;
import com.gestormemoria.memoriaservice.dto.*;
import com.gestormemoria.memoriaservice.exception.SecuenciaInvalidaException;
import com.gestormemoria.memoriaservice.exception.SecuenciaNoEncontradaException;
import com.gestormemoria.memoriaservice.exception.CorridaNoEncontradaException;
import com.gestormemoria.memoriaservice.model.*;
import com.gestormemoria.memoriaservice.repository.CorridaRepository;
import com.gestormemoria.memoriaservice.repository.SecuenciaRepository;
import java.util.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;

@Service
public class CorridaService {
    private final MapaService mapas;
    private final SecuenciaRepository secuencias;
    private final CorridaRepository corridas;
    private final AsignadorClient asignador;
    private final ObjectMapper json;

    public CorridaService(MapaService mapas, SecuenciaRepository secuencias, CorridaRepository corridas,
                          AsignadorClient asignador, ObjectMapper json) {
        this.mapas = mapas; this.secuencias = secuencias; this.corridas = corridas; this.asignador = asignador; this.json = json;
    }

    @Transactional
    public List<CorridaResponse> ejecutar(CorridaRequest request, Authentication auth) {
        var mapa = mapas.exigir(request.mapaId());
        var secuencia = secuencias.findById(request.secuenciaId())
                .orElseThrow(() -> new SecuenciaNoEncontradaException(request.secuenciaId()));
        String propietario = auth.getName();
        List<CorridaResponse> respuestas = new ArrayList<>();
        Set<Integer> unicas = new LinkedHashSet<>(request.estrategias());
        for (Integer codigo : unicas) {
            String estrategia = String.valueOf(codigo);
            Map<String, Object> resultado;
            try {
                resultado = asignador.simular(mapa, secuencia, codigo);
            } catch (RestClientResponseException ex) {
                if (ex.getStatusCode().is4xxClientError())
                    throw new SecuenciaInvalidaException("No se pudo ejecutar la secuencia: alguna operación no es válida o contradice el estado de memoria.");
                throw ex;
            }
            try {
                Corrida corrida = corridas.save(new Corrida(mapa, secuencia, estrategia, propietario, json.writeValueAsString(resultado)));
                respuestas.add(respuesta(corrida, json.valueToTree(resultado)));
            } catch (Exception ex) {
                throw new IllegalStateException("No se pudo guardar el resultado de la corrida.", ex);
            }
        }
        return respuestas;
    }

    @Transactional(readOnly = true)
    public CorridaResponse obtener(Long id, Authentication auth) {
        Corrida corrida = corridas.findById(id).orElseThrow(() -> new CorridaNoEncontradaException(id));
        if (!corrida.getPropietario().equals(auth.getName()))
            throw new org.springframework.security.access.AccessDeniedException("No tienes permiso para consultar esta corrida.");
        try { return respuesta(corrida, json.readTree(corrida.getResultadoJson())); }
        catch (Exception ex) { throw new IllegalStateException("No se pudo leer el resultado guardado.", ex); }
    }

    @Transactional(readOnly = true)
    public List<CorridaResponse> historial(Authentication auth) {
        return corridas.findAllByPropietarioOrderByCreadaEnDesc(auth.getName()).stream().map(c -> {
            try { return respuesta(c, json.readTree(c.getResultadoJson())); }
            catch (Exception ex) { throw new IllegalStateException(ex); }
        }).toList();
    }

    private CorridaResponse respuesta(Corrida c, JsonNode resultado) {
        return new CorridaResponse(c.getId(), c.getMapa().getId(), c.getSecuencia().getId(), c.getEstrategia(), c.getPropietario(), c.getCreadaEn(), resultado);
    }
}

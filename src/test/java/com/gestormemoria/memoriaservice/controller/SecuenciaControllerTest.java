package com.gestormemoria.memoriaservice.controller;

import tools.jackson.databind.ObjectMapper;
import com.gestormemoria.memoriaservice.dto.CrearSecuenciaRequest;
import com.gestormemoria.memoriaservice.dto.OperacionRequest;
import com.gestormemoria.memoriaservice.dto.OperacionResponse;
import com.gestormemoria.memoriaservice.dto.SecuenciaResponse;
import com.gestormemoria.memoriaservice.entity.TipoOperacion;
import com.gestormemoria.memoriaservice.service.SecuenciaService;
import com.gestormemoria.memoriaservice.service.SecuenciaNoEncontradaException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SecuenciaController.class)
class SecuenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SecuenciaService secuenciaService;

    @Test
    void debeCrearSecuenciaYRetornar201() throws Exception {

        // Arrange: preparamos la petición
        CrearSecuenciaRequest request = new CrearSecuenciaRequest(
                "Secuencia de prueba",
                List.of(
                        new OperacionRequest(
                                TipoOperacion.ASIGNAR, "P1", 100)
                )
        );

        // Simulamos la respuesta del servicio
        SecuenciaResponse respuesta = new SecuenciaResponse(
                1L,
                "Secuencia de prueba",
                List.of(
                        new OperacionResponse(
                                1, TipoOperacion.ASIGNAR, "P1", 100)
                )
        );

        when(secuenciaService.crearSecuencia(any(CrearSecuenciaRequest.class)))
                .thenReturn(respuesta);

        // Act: enviamos una petición POST simulada
        // Assert: verificamos la respuesta HTTP
        mockMvc.perform(post("/secuencias")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Secuencia de prueba"))
                .andExpect(jsonPath("$.operaciones.length()").value(1))
                .andExpect(jsonPath("$.operaciones[0].tipo").value("ASIGNAR"));
    }

    @Test
    void debeConsultarSecuenciaYRetornar200() throws Exception {

        // Arrange
        SecuenciaResponse respuesta = new SecuenciaResponse(
                1L,
                "Secuencia de consulta",
                List.of(
                        new OperacionResponse(
                                1, TipoOperacion.ASIGNAR, "P1", 100),
                        new OperacionResponse(
                                2, TipoOperacion.LIBERAR, "P1", null)
                )
        );

        when(secuenciaService.obtenerSecuencia(1L))
                .thenReturn(respuesta);

        // Act y Assert
        mockMvc.perform(get("/secuencias/1")
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Secuencia de consulta"))
                .andExpect(jsonPath("$.operaciones.length()").value(2))
                .andExpect(jsonPath("$.operaciones[0].posicion").value(1))
                .andExpect(jsonPath("$.operaciones[1].tipo").value("LIBERAR"));
    }

    @Test
    void debeRetornar404CuandoLaSecuenciaNoExiste() throws Exception {

        // Arrange
        when(secuenciaService.obtenerSecuencia(999L))
                .thenThrow(
                        new SecuenciaNoEncontradaException(
                                "No existe la secuencia con ID 999"
                        )
                );

        // Act y Assert
        mockMvc.perform(get("/secuencias/999")
                        .with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("No existe la secuencia con ID 999"));
    }


}

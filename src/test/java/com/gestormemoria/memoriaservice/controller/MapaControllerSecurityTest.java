package com.gestormemoria.memoriaservice.controller;

import com.gestormemoria.memoriaservice.config.SecurityConfig;
import com.gestormemoria.memoriaservice.dto.MapaResponse;
import com.gestormemoria.memoriaservice.service.MapaService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MapaController.class)
@Import(SecurityConfig.class)
class MapaControllerSecurityTest {
    @Autowired MockMvc mvc;
    @MockitoBean MapaService service;

    @Test
    void requiereAutenticacionParaConsultarMapas() throws Exception {
        mvc.perform(get("/mapas")).andExpect(status().isUnauthorized());
    }

    @Test
    void estudiantePuedeCrearMapa() throws Exception {
        when(service.crear(any())).thenReturn(new MapaResponse(1L, "mapa", List.of(new MapaResponse.Bloque(0, 100))));
        mvc.perform(post("/mapas").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"mapa\",\"bloques\":[{\"inicio\":0,\"tamano\":100}]}"))
                .andExpect(status().isCreated());
    }

    @Test
    void estudiantePuedeEliminarMapa() throws Exception {
        mvc.perform(delete("/mapas/1").with(jwt()))
                .andExpect(status().isNoContent());
        verify(service).eliminar(1L);
    }
}

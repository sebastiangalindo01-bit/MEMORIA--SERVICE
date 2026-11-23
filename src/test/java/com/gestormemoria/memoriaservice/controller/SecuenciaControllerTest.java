package com.gestormemoria.memoriaservice.controller;

import tools.jackson.databind.ObjectMapper;
import com.gestormemoria.memoriaservice.config.SecurityConfig;
import com.gestormemoria.memoriaservice.dto.*;
import com.gestormemoria.memoriaservice.model.TipoOperacion;
import com.gestormemoria.memoriaservice.service.SecuenciaService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SecuenciaController.class)
@Import(SecurityConfig.class)
class SecuenciaControllerTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @MockitoBean SecuenciaService service;

    @Test
    void creaSecuenciaConOperacionesOrdenadas() throws Exception {
        var request = new SecuenciaRequest("carga", null, List.of(new OperacionRequest(TipoOperacion.ASIGNAR, 1, 80)));
        var response = new SecuenciaResponse(3L, "carga", null, 1,
                List.of(new OperacionResponse(1, TipoOperacion.ASIGNAR, 1, 80)));
        when(service.crear(any(SecuenciaRequest.class))).thenReturn(response);
        mvc.perform(post("/secuencias").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ESTUDIANTE")))
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.operaciones[0].orden").value(1));
    }

    @Test
    void devuelveLaSecuencia() throws Exception {
        when(service.obtener(3L)).thenReturn(new SecuenciaResponse(3L, "carga", null, 1,
                List.of(new OperacionResponse(1, TipoOperacion.LIBERAR, 1, null))));
        mvc.perform(get("/secuencias/3").with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ESTUDIANTE"))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.operaciones[0].tipo").value("LIBERAR"));
    }
}

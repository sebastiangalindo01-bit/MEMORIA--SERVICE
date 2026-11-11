package com.gestormemoria.memoriaservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.gestormemoria.memoriaservice.dto.OperacionRequest;
import com.gestormemoria.memoriaservice.dto.SecuenciaRequest;
import com.gestormemoria.memoriaservice.dto.SecuenciaResponse;
import com.gestormemoria.memoriaservice.exception.SecuenciaInvalidaException;
import com.gestormemoria.memoriaservice.model.Secuencia;
import com.gestormemoria.memoriaservice.model.TipoOperacion;
import com.gestormemoria.memoriaservice.repository.SecuenciaRepository;
import java.util.List;
import org.junit.jupiter.api.Test;

class SecuenciaServiceTest {

    private final SecuenciaRepository repo = mock(SecuenciaRepository.class);
    private final SecuenciaService servicio = new SecuenciaService(repo);

    @Test
    void conservaElOrdenIngresado() {
        when(repo.save(any(Secuencia.class))).thenAnswer(i -> i.getArgument(0));
        SecuenciaRequest req = new SecuenciaRequest("s1", null, List.of(
                new OperacionRequest(TipoOperacion.ASIGNAR, 1, 64),
                new OperacionRequest(TipoOperacion.ASIGNAR, 2, 32),
                new OperacionRequest(TipoOperacion.LIBERAR, 1, null)));

        SecuenciaResponse r = servicio.crear(req);

        assertEquals(3, r.total());
        assertEquals(1, r.operaciones().get(0).orden());
        assertEquals(TipoOperacion.LIBERAR, r.operaciones().get(2).tipo());
        assertNull(r.operaciones().get(2).tamano());
    }

    @Test
    void asignarSinTamanoSeRechazaConMensajeClaro() {
        SecuenciaRequest req = new SecuenciaRequest("s1", null, List.of(
                new OperacionRequest(TipoOperacion.ASIGNAR, 1, null)));

        SecuenciaInvalidaException ex = assertThrows(SecuenciaInvalidaException.class, () -> servicio.crear(req));
        assertTrue(ex.getMessage().contains("operación 1"));
    }
}

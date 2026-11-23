package com.gestormemoria.memoriaservice.service;

import com.gestormemoria.memoriaservice.dto.OperacionRequest;
import com.gestormemoria.memoriaservice.dto.SecuenciaRequest;
import com.gestormemoria.memoriaservice.exception.SecuenciaInvalidaException;
import com.gestormemoria.memoriaservice.model.Secuencia;
import com.gestormemoria.memoriaservice.model.TipoOperacion;
import com.gestormemoria.memoriaservice.repository.SecuenciaRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SecuenciaServiceTest {
    private final SecuenciaRepository repo = mock(SecuenciaRepository.class);
    private final SecuenciaService servicio = new SecuenciaService(repo);

    @Test
    void guardaLaSecuenciaYConservaElOrden() {
        when(repo.save(any(Secuencia.class))).thenAnswer(call -> call.getArgument(0));
        var respuesta = servicio.crear(new SecuenciaRequest("carga", null, List.of(
                new OperacionRequest(TipoOperacion.ASIGNAR, 7, 64),
                new OperacionRequest(TipoOperacion.LIBERAR, 7, null))));
        assertEquals(2, respuesta.total());
        assertEquals(1, respuesta.operaciones().get(0).orden());
        assertEquals(2, respuesta.operaciones().get(1).orden());
        assertNull(respuesta.operaciones().get(1).tamano());
        verify(repo).save(any(Secuencia.class));
    }

    @Test
    void rechazaAsignacionesSinTamanoPositivo() {
        assertThrows(SecuenciaInvalidaException.class, () -> servicio.crear(new SecuenciaRequest("carga", null,
                List.of(new OperacionRequest(TipoOperacion.ASIGNAR, 7, 0)))));
        verifyNoInteractions(repo);
    }
}

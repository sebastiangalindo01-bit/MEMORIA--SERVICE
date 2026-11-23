package com.gestormemoria.memoriaservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.gestormemoria.memoriaservice.dto.OperacionRequest;
import com.gestormemoria.memoriaservice.dto.OperacionResponse;
import com.gestormemoria.memoriaservice.dto.SecuenciaRequest;
import com.gestormemoria.memoriaservice.dto.SecuenciaResponse;
import com.gestormemoria.memoriaservice.exception.SecuenciaInvalidaException;
import com.gestormemoria.memoriaservice.model.Secuencia;
import com.gestormemoria.memoriaservice.model.TipoOperacion;
import com.gestormemoria.memoriaservice.repository.SecuenciaRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** HU-03: registrar solicitudes de liberacion de memoria (RF-02). */
class SecuenciaLiberacionTest {

    private final SecuenciaRepository repo = mock(SecuenciaRepository.class);
    private final SecuenciaService servicio = new SecuenciaService(repo);
    private final Validator validador = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeEach
    void guardarDevuelveLoMismo() {
        when(repo.save(any(Secuencia.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void liberarSeRegistraSoloConElProceso() {
        SecuenciaResponse r = servicio.crear(new SecuenciaRequest("s", null, List.of(
                new OperacionRequest(TipoOperacion.LIBERAR, 7, null))));

        OperacionResponse op = r.operaciones().get(0);
        assertEquals(1, op.orden());
        assertEquals(TipoOperacion.LIBERAR, op.tipo());
        assertEquals(7, op.proceso());
        assertNull(op.tamano());
    }

    @Test
    void liberarDescartaElTamanoSiSeEnvia() {
        SecuenciaResponse r = servicio.crear(new SecuenciaRequest("s", null, List.of(
                new OperacionRequest(TipoOperacion.LIBERAR, 7, 100))));

        assertNull(r.operaciones().get(0).tamano());
    }

    @Test
    void mezclaAsignarYLiberarConservaOrdenYNumeracion() {
        SecuenciaResponse r = servicio.crear(new SecuenciaRequest("s", null, List.of(
                new OperacionRequest(TipoOperacion.ASIGNAR, 1, 64),
                new OperacionRequest(TipoOperacion.ASIGNAR, 2, 32),
                new OperacionRequest(TipoOperacion.LIBERAR, 1, null),
                new OperacionRequest(TipoOperacion.ASIGNAR, 3, 16),
                new OperacionRequest(TipoOperacion.LIBERAR, 2, null))));

        assertEquals(5, r.total());
        for (int i = 0; i < 5; i++) {
            assertEquals(i + 1, r.operaciones().get(i).orden());
        }
        assertEquals(List.of(TipoOperacion.ASIGNAR, TipoOperacion.ASIGNAR, TipoOperacion.LIBERAR,
                        TipoOperacion.ASIGNAR, TipoOperacion.LIBERAR),
                r.operaciones().stream().map(OperacionResponse::tipo).toList());
        assertEquals(List.of(1, 2, 1, 3, 2),
                r.operaciones().stream().map(OperacionResponse::proceso).toList());
    }

    @Test
    void liberarConProcesoNegativoSeRechaza() {
        SecuenciaRequest req = new SecuenciaRequest("s", null, List.of(
                new OperacionRequest(TipoOperacion.ASIGNAR, 1, 64),
                new OperacionRequest(TipoOperacion.LIBERAR, -1, null)));

        SecuenciaInvalidaException ex =
                assertThrows(SecuenciaInvalidaException.class, () -> servicio.crear(req));
        assertTrue(ex.getMessage().contains("2, el proceso no puede ser negativo"));
    }

    @Test
    void liberarSinProcesoFallaLaValidacion() {
        SecuenciaRequest req = new SecuenciaRequest("s", null, List.of(
                new OperacionRequest(TipoOperacion.LIBERAR, null, null)));

        Set<ConstraintViolation<SecuenciaRequest>> violaciones = validador.validate(req);

        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().contains("proceso")));
    }

    @Test
    void liberarConProcesoInformadoPasaLaValidacion() {
        SecuenciaRequest req = new SecuenciaRequest("s", null, List.of(
                new OperacionRequest(TipoOperacion.LIBERAR, 4, null)));

        assertTrue(validador.validate(req).isEmpty());
    }
}
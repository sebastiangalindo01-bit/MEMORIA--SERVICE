package com.gestormemoria.memoriaservice.service;

import com.gestormemoria.memoriaservice.dto.*;
import com.gestormemoria.memoriaservice.entity.*;
import com.gestormemoria.memoriaservice.repository.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecuenciaServiceTest {

    @Mock
    private SecuenciaRepository secuenciaRepository;

    @Mock
    private OperacionRepository operacionRepository;

    @InjectMocks
    private SecuenciaService secuenciaService;

    @Test
    void debeCrearSecuenciaConOperacionesEnOrden() {

        // Arrange: preparamos los datos de prueba
        CrearSecuenciaRequest request = new CrearSecuenciaRequest(
                "Secuencia de prueba",
                List.of(
                        new OperacionRequest(
                                TipoOperacion.ASIGNAR, "P1", 100),
                        new OperacionRequest(
                                TipoOperacion.LIBERAR, "P1", null),
                        new OperacionRequest(
                                TipoOperacion.ASIGNAR, "P2", 200)
                )
        );

        when(secuenciaRepository.save(any(Secuencia.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(operacionRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act: ejecutamos el método que queremos probar
        SecuenciaResponse respuesta =
                secuenciaService.crearSecuencia(request);

        // Assert: comprobamos el resultado
        assertNotNull(respuesta);
        assertEquals("Secuencia de prueba", respuesta.nombre());
        assertEquals(3, respuesta.operaciones().size());

        assertEquals(1, respuesta.operaciones().get(0).posicion());
        assertEquals(TipoOperacion.ASIGNAR,
                respuesta.operaciones().get(0).tipo());

        assertEquals(2, respuesta.operaciones().get(1).posicion());
        assertEquals(TipoOperacion.LIBERAR,
                respuesta.operaciones().get(1).tipo());

        assertEquals(3, respuesta.operaciones().get(2).posicion());
        assertEquals(TipoOperacion.ASIGNAR,
                respuesta.operaciones().get(2).tipo());

        verify(secuenciaRepository, times(1))
                .save(any(Secuencia.class));

        verify(operacionRepository, times(1))
                .saveAll(anyList());
    }

    @Test
    void debeRechazarSecuenciaSinNombre() {

        // Arrange
        CrearSecuenciaRequest request = new CrearSecuenciaRequest(
                " ",
                List.of(
                        new OperacionRequest(
                                TipoOperacion.ASIGNAR, "P1", 100)
                )
        );

        // Act y Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> secuenciaService.crearSecuencia(request)
        );

        // Verificamos que no se guarde nada
        verifyNoInteractions(secuenciaRepository, operacionRepository);
    }

    @Test
    void debeRechazarAsignacionConTamanoCero() {

        // Arrange
        CrearSecuenciaRequest request = new CrearSecuenciaRequest(
                "Secuencia inválida",
                List.of(
                        new OperacionRequest(
                                TipoOperacion.ASIGNAR, "P1", 0)
                )
        );

        // Act y Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> secuenciaService.crearSecuencia(request)
        );

        verifyNoInteractions(secuenciaRepository, operacionRepository);
    }

    @Test
    void debeRechazarLiberacionConTamano() {

        // Arrange
        CrearSecuenciaRequest request = new CrearSecuenciaRequest(
                "Secuencia inválida",
                List.of(
                        new OperacionRequest(
                                TipoOperacion.LIBERAR, "P1", 100)
                )
        );

        // Act y Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> secuenciaService.crearSecuencia(request)
        );

        verifyNoInteractions(secuenciaRepository, operacionRepository);
    }

    @Test
    void debeObtenerSecuenciaConOperacionesEnOrden() {

        // Arrange: creamos una secuencia simulada
        Secuencia secuencia = new Secuencia();
        secuencia.setId(1L);
        secuencia.setNombre("Secuencia de consulta");

        Operacion primera = new Operacion();
        primera.setPosicion(1);
        primera.setTipo(TipoOperacion.ASIGNAR);
        primera.setProcesoId("P1");
        primera.setTamanoKb(100);

        Operacion segunda = new Operacion();
        segunda.setPosicion(2);
        segunda.setTipo(TipoOperacion.LIBERAR);
        segunda.setProcesoId("P1");
        segunda.setTamanoKb(null);

        when(secuenciaRepository.findById(1L))
                .thenReturn(Optional.of(secuencia));

        when(operacionRepository.findBySecuenciaIdOrderByPosicionAsc(1L))
                .thenReturn(List.of(primera, segunda));

        // Act: consultamos la secuencia
        SecuenciaResponse respuesta =
                secuenciaService.obtenerSecuencia(1L);

        // Assert: verificamos el resultado
        assertNotNull(respuesta);
        assertEquals(1L, respuesta.id());
        assertEquals("Secuencia de consulta", respuesta.nombre());
        assertEquals(2, respuesta.operaciones().size());

        assertEquals(1, respuesta.operaciones().get(0).posicion());
        assertEquals(TipoOperacion.ASIGNAR,
                respuesta.operaciones().get(0).tipo());

        assertEquals(2, respuesta.operaciones().get(1).posicion());
        assertEquals(TipoOperacion.LIBERAR,
                respuesta.operaciones().get(1).tipo());

        verify(secuenciaRepository).findById(1L);
        verify(operacionRepository)
                .findBySecuenciaIdOrderByPosicionAsc(1L);
    }

    @Test
    void debeLanzarExcepcionCuandoLaSecuenciaNoExiste() {

        // Arrange: simulamos que no existe la secuencia
        when(secuenciaRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act y Assert: esperamos la excepción correspondiente
        assertThrows(
                SecuenciaNoEncontradaException.class,
                () -> secuenciaService.obtenerSecuencia(999L)
        );

        // No debe intentar consultar las operaciones
        verify(secuenciaRepository).findById(999L);

        verifyNoInteractions(operacionRepository);
    }

}

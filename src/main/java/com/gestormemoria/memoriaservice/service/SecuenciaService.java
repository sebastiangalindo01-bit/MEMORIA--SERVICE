package com.gestormemoria.memoriaservice.service;

import com.gestormemoria.memoriaservice.dto.*;
import com.gestormemoria.memoriaservice.entity.*;
import com.gestormemoria.memoriaservice.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecuenciaService {

    private final SecuenciaRepository secuenciaRepository;
    private final OperacionRepository operacionRepository;

    public SecuenciaService(
            SecuenciaRepository secuenciaRepository,
            OperacionRepository operacionRepository
    ) {
        this.secuenciaRepository = secuenciaRepository;
        this.operacionRepository = operacionRepository;
    }

    @Transactional
    public SecuenciaResponse crearSecuencia(CrearSecuenciaRequest request) {

        if (request == null ||
                request.nombre() == null ||
                request.nombre().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la secuencia es obligatorio"
            );
        }

        if (request.nombre().trim().length() > 100) {
            throw new IllegalArgumentException(
                    "El nombre no puede superar 100 caracteres"
            );
        }

        if (request.operaciones() == null ||
                request.operaciones().isEmpty()) {
            throw new IllegalArgumentException(
                    "La secuencia debe contener operaciones"
            );
        }

        // Validar todas las operaciones antes de guardarlas
        for (OperacionRequest operacion : request.operaciones()) {
            validarOperacion(operacion);
        }

        // Crear y guardar la secuencia
        Secuencia secuencia = new Secuencia();
        secuencia.setNombre(request.nombre().trim());

        Secuencia secuenciaGuardada =
                secuenciaRepository.save(secuencia);

        List<Operacion> operaciones = new ArrayList<>();

        int posicion = 1;

        for (OperacionRequest operacionRequest : request.operaciones()) {

            Operacion operacion = new Operacion();

            operacion.setTipo(operacionRequest.tipo());
            operacion.setProcesoId(
                    operacionRequest.procesoId().trim()
            );
            operacion.setTamanoKb(
                    operacionRequest.tipo() == TipoOperacion.ASIGNAR
                            ? operacionRequest.tamanoKb()
                            : null
            );
            operacion.setPosicion(posicion);
            operacion.setSecuencia(secuenciaGuardada);

            operaciones.add(operacion);
            posicion++;
        }

        operacionRepository.saveAll(operaciones);

        return construirRespuesta(secuenciaGuardada, operaciones);
    }

    @Transactional(readOnly = true)
    public SecuenciaResponse obtenerSecuencia(Long id) {

        Secuencia secuencia = secuenciaRepository.findById(id)
                .orElseThrow(() ->
                        new SecuenciaNoEncontradaException(
                                "No existe la secuencia con ID " + id
                        )
                );

        List<Operacion> operaciones =
                operacionRepository.findBySecuenciaIdOrderByPosicionAsc(id);

        return construirRespuesta(secuencia, operaciones);
    }

    private void validarOperacion(OperacionRequest operacion) {

        if (operacion == null || operacion.tipo() == null) {
            throw new IllegalArgumentException(
                    "El tipo de operación es obligatorio"
            );
        }

        if (operacion.procesoId() == null ||
                operacion.procesoId().isBlank()) {
            throw new IllegalArgumentException(
                    "El identificador del proceso es obligatorio"
            );
        }

        if (operacion.procesoId().trim().length() > 50) {
            throw new IllegalArgumentException(
                    "El identificador del proceso no puede superar 50 caracteres"
            );
        }

        if (operacion.tipo() == TipoOperacion.ASIGNAR) {

            if (operacion.tamanoKb() == null ||
                    operacion.tamanoKb() <= 0) {
                throw new IllegalArgumentException(
                        "La asignación debe tener un tamaño mayor que cero"
                );
            }

        } else if (operacion.tamanoKb() != null) {

            throw new IllegalArgumentException(
                    "Una operación de liberación no debe incluir tamaño"
            );
        }
    }

    private SecuenciaResponse construirRespuesta(
            Secuencia secuencia,
            List<Operacion> operaciones
    ) {

        List<OperacionResponse> operacionesResponse =
                operaciones.stream()
                        .map(op -> new OperacionResponse(
                                op.getPosicion(),
                                op.getTipo(),
                                op.getProcesoId(),
                                op.getTamanoKb()
                        ))
                        .toList();

        return new SecuenciaResponse(
                secuencia.getId(),
                secuencia.getNombre(),
                operacionesResponse
        );
    }
}

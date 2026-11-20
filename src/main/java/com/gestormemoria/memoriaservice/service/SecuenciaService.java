package com.gestormemoria.memoriaservice.service;

import com.gestormemoria.memoriaservice.dto.OperacionRequest;
import com.gestormemoria.memoriaservice.dto.SecuenciaRequest;
import com.gestormemoria.memoriaservice.dto.SecuenciaResponse;
import com.gestormemoria.memoriaservice.exception.SecuenciaInvalidaException;
import com.gestormemoria.memoriaservice.exception.SecuenciaNoEncontradaException;
import com.gestormemoria.memoriaservice.model.Operacion;
import com.gestormemoria.memoriaservice.model.Secuencia;
import com.gestormemoria.memoriaservice.model.TipoOperacion;
import com.gestormemoria.memoriaservice.repository.SecuenciaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecuenciaService {

    private final SecuenciaRepository repositorio;

    public SecuenciaService(SecuenciaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional
    public SecuenciaResponse crear(SecuenciaRequest req) {
        validarOperaciones(req.operaciones());

        Secuencia secuencia = new Secuencia(req.nombre().trim(), req.semilla());
        int orden = 1; // el orden de llegada es el orden de la secuencia: no se reordena nada
        for (OperacionRequest o : req.operaciones()) {
            Integer tamano = o.tipo() == TipoOperacion.ASIGNAR ? o.tamano() : null;
            secuencia.agregar(new Operacion(orden++, o.tipo(), o.proceso(), tamano));
        }
        return SecuenciaResponse.de(repositorio.save(secuencia));
    }

    @Transactional(readOnly = true)
    public SecuenciaResponse obtener(Long id) {
        return repositorio.findById(id)
                .map(SecuenciaResponse::de)
                .orElseThrow(() -> new SecuenciaNoEncontradaException(id));
    }

    @Transactional(readOnly = true)
    public List<SecuenciaResponse> listar() {
        return repositorio.findAll().stream().map(SecuenciaResponse::de).toList();
    }

    private void validarOperaciones(List<OperacionRequest> ops) {
        for (int i = 0; i < ops.size(); i++) {
            OperacionRequest o = ops.get(i);
            int posicion = i + 1;
            if (o.proceso() < 0) {
                throw new SecuenciaInvalidaException(
                        "En la operación " + posicion + ", el proceso no puede ser negativo.");
            }
            if (o.tipo() == TipoOperacion.ASIGNAR && (o.tamano() == null || o.tamano() <= 0)) {
                throw new SecuenciaInvalidaException(
                        "En la operación " + posicion + ", asignar necesita un tamaño en KB mayor que 0.");
            }
        }
    }
}

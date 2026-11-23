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

    public SecuenciaService(SecuenciaRepository repositorio) { this.repositorio = repositorio; }

    @Transactional
    public SecuenciaResponse crear(SecuenciaRequest req) {
        validarOperaciones(req.operaciones());
        Secuencia secuencia = new Secuencia(req.nombre().trim(), req.semilla());
        int orden = 1;
        for (OperacionRequest op : req.operaciones()) {
            Integer tamano = op.tipo() == TipoOperacion.ASIGNAR ? op.tamano() : null;
            secuencia.agregar(new Operacion(orden++, op.tipo(), op.proceso(), tamano));
        }
        return SecuenciaResponse.de(repositorio.save(secuencia));
    }

    @Transactional(readOnly = true)
    public SecuenciaResponse obtener(Long id) {
        return repositorio.findById(id).map(SecuenciaResponse::de)
                .orElseThrow(() -> new SecuenciaNoEncontradaException(id));
    }

    @Transactional(readOnly = true)
    public List<SecuenciaResponse> listar() {
        return repositorio.findAll().stream().map(SecuenciaResponse::de).toList();
    }

    private void validarOperaciones(List<OperacionRequest> ops) {
        for (int i = 0; i < ops.size(); i++) {
            OperacionRequest op = ops.get(i);
            if (op.proceso() < 0) throw new SecuenciaInvalidaException("En la operación " + (i + 1) + ", el proceso no puede ser negativo.");
            if (op.tipo() == TipoOperacion.ASIGNAR && (op.tamano() == null || op.tamano() <= 0))
                throw new SecuenciaInvalidaException("La operación " + (i + 1) + " debe asignar un tamaño mayor que cero.");
        }
    }
}

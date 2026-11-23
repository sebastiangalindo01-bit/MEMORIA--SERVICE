package com.gestormemoria.memoriaservice.repository;

import com.gestormemoria.memoriaservice.entity.Operacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperacionRepository extends JpaRepository<Operacion, Long> {

    List<Operacion> findBySecuenciaIdOrderByPosicionAsc(Long secuenciaId);
}

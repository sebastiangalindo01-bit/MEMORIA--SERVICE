package com.gestormemoria.memoriaservice.repository;

import com.gestormemoria.memoriaservice.model.Corrida;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorridaRepository extends JpaRepository<Corrida, Long> {
    List<Corrida> findAllByPropietarioOrderByCreadaEnDesc(String propietario);
    boolean existsByMapa_Id(Long mapaId);
}

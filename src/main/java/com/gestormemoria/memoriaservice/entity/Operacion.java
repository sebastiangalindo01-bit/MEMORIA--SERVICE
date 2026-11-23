
package com.gestormemoria.memoriaservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "operacion",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"id_secuencia", "posicion"}
                )
        }
)
@Getter
@Setter
public class Operacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operacion")
    private Long id;

    @Column(nullable = false)
    private Integer posicion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoOperacion tipo;

    @Column(name = "proceso_id", nullable = false, length = 50)
    private String procesoId;

    @Column(name = "tamano_kb")
    private Integer tamanoKb;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_secuencia", nullable = false)
    private Secuencia secuencia;
}

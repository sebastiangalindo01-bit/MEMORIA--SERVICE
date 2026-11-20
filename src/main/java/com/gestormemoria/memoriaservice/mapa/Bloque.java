package com.gestormemoria.memoriaservice.mapa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bloques")
@Getter
@Setter
@NoArgsConstructor
public class Bloque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Direccion de inicio del bloque (entero). */
    @Column(nullable = false)
    private int inicio;

    /** Tamano del bloque en KB (entero, mayor que cero). */
    @Column(name = "tamano_kb", nullable = false)
    private int tamanoKb;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mapa_id")
    private MapaMemoria mapa;
}

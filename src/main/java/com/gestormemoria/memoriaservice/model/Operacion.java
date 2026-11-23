package com.gestormemoria.memoriaservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "operaciones")
public class Operacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Posición dentro de la secuencia (1..n). Es lo que garantiza que el orden no se pierda. */
    @Column(nullable = false)
    private int orden;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoOperacion tipo;

    @Column(nullable = false)
    private int proceso;

    /** KB pedidos. Nulo cuando la operación es LIBERAR. */
    private Integer tamano;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "secuencia_id")
    private Secuencia secuencia;

    protected Operacion() { }

    public Operacion(int orden, TipoOperacion tipo, int proceso, Integer tamano) {
        this.orden = orden;
        this.tipo = tipo;
        this.proceso = proceso;
        this.tamano = tamano;
    }

    public Long getId() { return id; }
    public int getOrden() { return orden; }
    public TipoOperacion getTipo() { return tipo; }
    public int getProceso() { return proceso; }
    public Integer getTamano() { return tamano; }
    public Secuencia getSecuencia() { return secuencia; }
    void setSecuencia(Secuencia secuencia) { this.secuencia = secuencia; }
}

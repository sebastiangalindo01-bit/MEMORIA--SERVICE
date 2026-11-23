package com.gestormemoria.memoriaservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bloques_mapa")
public class BloqueMapa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private int orden;
    @Column(nullable = false) private int inicio;
    @Column(nullable = false) private int tamano;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mapa_id", nullable = false)
    private MapaMemoria mapa;

    protected BloqueMapa() { }
    public BloqueMapa(int orden, int inicio, int tamano) { this.orden = orden; this.inicio = inicio; this.tamano = tamano; }
    void setMapa(MapaMemoria mapa) { this.mapa = mapa; }
    public int getOrden() { return orden; }
    public int getInicio() { return inicio; }
    public int getTamano() { return tamano; }
}

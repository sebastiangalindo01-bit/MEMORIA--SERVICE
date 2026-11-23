package com.gestormemoria.memoriaservice.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "corridas")
public class Corrida {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "mapa_id")
    private MapaMemoria mapa;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "secuencia_id")
    private Secuencia secuencia;
    @Column(nullable = false, length = 30) private String estrategia;
    @Column(nullable = false, length = 100) private String propietario;
    @Column(nullable = false) private Instant creadaEn;
    @Column(nullable = false, columnDefinition = "text") private String resultadoJson;

    protected Corrida() { }
    public Corrida(MapaMemoria mapa, Secuencia secuencia, String estrategia, String propietario, String resultadoJson) {
        this.mapa = mapa; this.secuencia = secuencia; this.estrategia = estrategia;
        this.propietario = propietario; this.resultadoJson = resultadoJson; this.creadaEn = Instant.now();
    }
    public Long getId() { return id; }
    public MapaMemoria getMapa() { return mapa; }
    public Secuencia getSecuencia() { return secuencia; }
    public String getEstrategia() { return estrategia; }
    public String getPropietario() { return propietario; }
    public Instant getCreadaEn() { return creadaEn; }
    public String getResultadoJson() { return resultadoJson; }
}

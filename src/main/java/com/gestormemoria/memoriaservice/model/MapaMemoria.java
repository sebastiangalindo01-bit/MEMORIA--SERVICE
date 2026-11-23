package com.gestormemoria.memoriaservice.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mapas_memoria")
public class MapaMemoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100, unique = true)
    private String nombre;
    @OneToMany(mappedBy = "mapa", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<BloqueMapa> bloques = new ArrayList<>();

    protected MapaMemoria() { }
    public MapaMemoria(String nombre) { this.nombre = nombre; }
    public void agregar(BloqueMapa bloque) { bloque.setMapa(this); bloques.add(bloque); }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public List<BloqueMapa> getBloques() { return bloques; }
}

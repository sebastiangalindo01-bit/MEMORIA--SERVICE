package com.gestormemoria.memoriaservice.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "secuencias")
public class Secuencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    /** Semilla usada si la secuencia fue generada (RF-10). Nula si se escribió a mano. */
    private Long semilla;

    @OneToMany(mappedBy = "secuencia", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<Operacion> operaciones = new ArrayList<>();

    protected Secuencia() { }

    public Secuencia(String nombre, Long semilla) {
        this.nombre = nombre;
        this.semilla = semilla;
    }

    public void agregar(Operacion op) {
        op.setSecuencia(this);
        operaciones.add(op);
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Long getSemilla() { return semilla; }
    public List<Operacion> getOperaciones() { return operaciones; }
}

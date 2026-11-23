package com.gestormemoria.memoriaservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "secuencia")
@Getter
@Setter
public class Secuencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_secuencia")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;
}

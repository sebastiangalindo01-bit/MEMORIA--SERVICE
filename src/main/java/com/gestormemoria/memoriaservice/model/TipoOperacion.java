package com.gestormemoria.memoriaservice.model;

public enum TipoOperacion {
    ASIGNAR(0),   // coincide con op_tipo en memoria.h
    LIBERAR(1);

    private final int codigo;

    TipoOperacion(int codigo) { this.codigo = codigo; }

    public int getCodigo() { return codigo; }
}

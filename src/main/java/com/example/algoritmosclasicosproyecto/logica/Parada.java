package com.example.algoritmosclasicosproyecto.logica;

import java.util.Objects;

public class Parada {
    private String id;
    private String nombre;

    public Parada(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parada parada = (Parada) o;
        return id.equals(parada.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


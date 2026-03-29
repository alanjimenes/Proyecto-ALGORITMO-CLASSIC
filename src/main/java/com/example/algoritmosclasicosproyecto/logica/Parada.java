package com.example.algoritmosclasicosproyecto.logica;

public class Parada {
    private int id;
    private String nombre;
    private double x;
    private double y;

    public Parada(int id, String nombre, double x, double y) {
        this.id = id;
        this.nombre = nombre;
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }

    public double getY() { return y; }

    public void setX(double x) { this.x = x; }

    public void setY(double y) { this.y = y; }

    public int getId() {
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
        return this.id == parada.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}


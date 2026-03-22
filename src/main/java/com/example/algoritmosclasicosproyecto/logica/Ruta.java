package com.example.algoritmosclasicosproyecto.logica;

public class Ruta {
    private Parada origen;
    private Parada destino;
    private double tiempo;
    private double distancia;
    private double costo;
    private int trasbordo;


    public Ruta(Parada origen, Parada destino, double tiempo, double distancia, double costo, int trasbordo) {
        this.origen = origen;
        this.destino = destino;
        this.tiempo = tiempo;
        this.distancia = distancia;
        this.costo = costo;
        this.trasbordo = trasbordo;
    }


    public double getPeso(String criterio) {
        switch (criterio.toLowerCase()) {
            case "tiempo":    return tiempo;
            case "distancia": return distancia;
            case "costo":     return costo;
            case "trasbordo": return trasbordo;
            default:          return tiempo;
        }
    }
    public Parada getDestino() {
        return destino;
    }

    public void setDestino(Parada destino) {
        this.destino = destino;
    }

    public Parada getOrigen() {
        return origen;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public int getTrasbordo() {
        return trasbordo;
    }

    public void setTrasbordo(int trasbordo) {
        this.trasbordo = trasbordo;
    }
}
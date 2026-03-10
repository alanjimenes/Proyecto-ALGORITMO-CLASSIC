package com.example.algoritmosclasicosproyecto.logica;

public class Ruta {
    private Parada origen;
    private Parada destino;
    private double tiempoMinuto;
    private double distanciaKm;
    private double costo;
    private boolean requiereTrasbordo;


    public Ruta(Parada origen, Parada destino, double tiempoMinuto, double distanciaKm, double costo, boolean requiereTrasbordo) {
        this.origen = origen;
        this.destino = destino;
        this.tiempoMinuto = tiempoMinuto;
        this.distanciaKm = distanciaKm;
        this.costo = costo;
        this.requiereTrasbordo = requiereTrasbordo;
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

    public double getTiempoMinuto() {
        return tiempoMinuto;
    }

    public void setTiempoMinuto(double tiempoMinuto) {
        this.tiempoMinuto = tiempoMinuto;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public boolean isRequiereTrasbordo() {
        return requiereTrasbordo;
    }

    public void setRequiereTrasbordo(boolean requiereTrasbordo) {
        this.requiereTrasbordo = requiereTrasbordo;
    }
}
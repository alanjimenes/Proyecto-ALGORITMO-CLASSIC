package com.example.algoritmosclasicosproyecto.logica;

import java.util.List;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Transporte redTransporte = new Transporte();

        System.out.println("=== 1. INICIALIZACIÓN Y CARGA DE BD ===");
        redTransporte.cargarDatosDesdeBD();

        System.out.println("\n=== 2. CREACIÓN DE RED DE PRUEBA ===");
        redTransporte.addParada("P1", "Estación Norte");
        redTransporte.addParada("P2", "Centro Comercial");
        redTransporte.addParada("P3", "Universidad");


        redTransporte.addRuta("P1", "P2", 10.0, 5.0, 35.0, false);
        redTransporte.addRuta("P2", "P3", 15.0, 8.0, 35.0, false);
        redTransporte.addRuta("P1", "P3", 45.0, 20.0, 50.0, false);

        System.out.println("\n=== 3. PRUEBA DE ALGORITMO (DIJKSTRA) ===");
        System.out.println("Buscando la ruta óptima en base al TIEMPO desde Estación Norte (P1) a Universidad (P3)...");

        List<Parada> rutaMasRapida = redTransporte.dijkstra("P1", "P3", "tiempo");

        if (rutaMasRapida != null && !rutaMasRapida.isEmpty()) {
            System.out.print("Resultado del Algoritmo: ");
            for (int i = 0; i < rutaMasRapida.size(); i++) {
                System.out.print(rutaMasRapida.get(i).getNombre());
                if (i < rutaMasRapida.size() - 1) System.out.print(" -> ");
            }
            System.out.println();
        } else {
            System.out.println("No se encontró ninguna conexión lógica.");
        }
    }
}


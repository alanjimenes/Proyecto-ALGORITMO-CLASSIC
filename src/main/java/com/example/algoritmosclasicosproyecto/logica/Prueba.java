package com.example.algoritmosclasicosproyecto.logica;

import com.example.algoritmosclasicosproyecto.algoritmos.*;

import java.util.List;


public class Prueba {

    private static void cargarDatos(Transporte t) {
        t.addParada("A", "Monumento");
        t.addParada("B", "Centro León");
        t.addParada("C", "PUCMM");
        t.addParada("D", "Las Colinas");
        t.addParada("E", "Los Cerros");
        t.addParada("F", "Los Jardines");

        t.addRuta("A", "B",  20,  2.5,  25, 0);
        t.addRuta("A", "C",   8,  5.0,  20, 0);
        t.addRuta("B", "C",  10,  3.5,  50, 0);
        t.addRuta("B", "D",   5,  4.0,  90, 1);
        t.addRuta("C", "D",   6,  2.0,  15, 0);
        t.addRuta("C", "E",  20,  8.0,   8, 0);
        t.addRuta("D", "E",  15,  6.0,  80, 0);
        t.addRuta("D", "F",  25,  1.5,  90, 0);
        t.addRuta("E", "F",   5,  4.5,   8, 1);
    }

    public static void main(String[] args) {
        Transporte t = Transporte.getInstancia();
        cargarDatos(t);

        System.out.println("Buscando rutas de Monumento a Los Jardines...");
        System.out.println();

        List<Parada> rutaRapida    = Dijkstra.dijkstra(t, "A", "F", "tiempo");
        List<Parada> rutaCorta     = Dijkstra.dijkstra(t, "A", "F", "distancia");
        List<Parada> rutaEconomica = Dijkstra.dijkstra(t, "A", "F", "costo");
        List<Parada> rutaTrasbordo = Dijkstra.dijkstra(t, "A", "F", "trasbordo");

        System.out.print("Ruta más rápida:       "); rutaRapida.forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println("| " + t.calcularTotalRuta(rutaRapida, "tiempo") + " min");
        System.out.print("Ruta más corta:        "); rutaCorta.forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println("| " + t.calcularTotalRuta(rutaCorta, "distancia") + " km");
        System.out.print("Ruta más económica:    "); rutaEconomica.forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println("| $" + t.calcularTotalRuta(rutaEconomica, "costo"));
        System.out.print("Menos trasbordos:      "); rutaTrasbordo.forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println("| " + (int) t.calcularTotalRuta(rutaTrasbordo, "trasbordo") + " trasbordos");

        System.out.println();
        System.out.println("Verificando con Bellman-Ford (maneja descuentos)...");
        System.out.print("Ruta más económica:    "); BellmanFord.bellmanFord(t, "A", "F", "costo").forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println();

        System.out.println();
        System.out.println("Verificando con Floyd-Warshall (todas las rutas)...");
        System.out.print("Ruta más rápida:       "); FloydWarshall.floydWarshall(t, "A", "F", "tiempo").forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println();

        System.out.println();
        System.out.println("Recorrido completo de la red desde Monumento...");
        System.out.print("BFS: "); BFS.bfs(t, "A").forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println();
        System.out.print("DFS: "); DFS.dfs(t, "A").forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println();
    }
}
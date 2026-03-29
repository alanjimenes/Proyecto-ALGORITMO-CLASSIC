package com.example.algoritmosclasicosproyecto.logica;

import com.example.algoritmosclasicosproyecto.algoritmos.Dijkstra;

import java.util.List;
import java.util.Map;


public class Prueba {
    public static void main(String[] args) {

            Transporte sistema = new Transporte();
            sistema.load_data();
/*
            System.out.println("=== INICIANDO PRUEBAS CRUD ===\n");

            // --- PRUEBAS DE PARADAS ---
            System.out.println(">>> Test: Agregando Paradas...");
            sistema.addParada( "Estación Central");
            sistema.addParada("Terminal Norte");
            sistema.addParada( "Puerto Madero");

            System.out.println("\n>>> Test: Editando Parada 2...");
            sistema.editParada(8, "Terminal Norte - Renovada");

            // --- PRUEBAS DE RUTAS ---
            System.out.println("\n>>> Test: Creando Rutas (Conectando el Grafo)...");

            sistema.addRuta(7, 8, 15.5, 12.0, 2.50, 0);
            // De Terminal Norte (2) a Puerto Madero (3)
            sistema.addRuta(8, 9, 20.0, 18.5, 3.75, 1);

            System.out.println("\n>>> Test: Editando parámetros de Ruta (1 -> 2)...");
            // Bajamos el costo y el tiempo de la primera ruta
            sistema.editRuta(7, 8, 12.0, 12.0, 2.00, 0);



            // --- PRUEBAS DE ELIMINACIÓN ---
            System.out.println("\n>>> Test: Eliminando Ruta 2 -> 3...");
            sistema.deleteRuta(7, 8);

            System.out.println("\n>>> Test: Eliminando Parada 3...");
            sistema.deleteParada(9);

            System.out.println("\n=== PRUEBAS FINALIZADAS ===");
        }
*/


        System.out.println("=== SINCRONIZANDO BASE DE DATOS ===");
        sistema.load_data();

        Map<Integer, Parada> mapa = sistema.getParadaMap();

        // Validación de seguridad
        if (mapa.size() < 2) {
            System.err.println("Error: Necesitas al menos 2 paradas en la base de datos para buscar una ruta.");
            return;
        }

        // 2. Obtener IDs válidos dinámicamente
        // Tomaremos la primera y la última parada que se hayan cargado en el mapa
        Object[] idsDisponibles = mapa.keySet().toArray();
        int idOrigen = (int) idsDisponibles[0];
        int idDestino = (int) idsDisponibles[idsDisponibles.length - 1];

        System.out.println("\n=== EJECUTANDO DIJKSTRA ===");
        System.out.println("Origen:  [" + idOrigen + "] " + mapa.get(idOrigen).getNombre());
        System.out.println("Destino: [" + idDestino + "] " + mapa.get(idDestino).getNombre());
        sistema.editRuta(7, 9, 12.5, 19.0, 7.50, 0);


        String[] criterios = {"tiempo", "distancia", "costo"};

        for (String criterio : criterios) {
            System.out.println("\nBuscando por mejor " + criterio.toUpperCase() + "...");
            List<Parada> camino = Dijkstra.dijkstra(sistema, idOrigen, idDestino, criterio);

            if (camino != null && !camino.isEmpty()) {

                System.out.print("Ruta: ");
                for (int i = 0; i < camino.size(); i++) {
                    System.out.print(camino.get(i).getNombre());
                    if (i < camino.size() - 1) System.out.print(" -> ");
                }


                double total = sistema.calcularTotalRuta(camino, criterio);
                System.out.println("\nTotal Acumulado (" + criterio + "): " + total);
            } else {
                System.err.println("No se encontró conexión para este criterio.");
            }
        }
    }
    }


  /*  private static void cargarDatos(Transporte t) {
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
        System.out.print("BFS: "); BFS.bfs(t, 1).forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println();
        System.out.print("DFS: "); DFS.dfs(t, 2).forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println();
    }*/

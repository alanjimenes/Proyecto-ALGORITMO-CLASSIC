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
            System.out.println(">>> Test: Agregando Paradas...");
            sistema.addParada( "Estación Central");
            sistema.addParada("Terminal Norte");
            sistema.addParada( "Puerto Madero");
            System.out.println("\n>>> Test: Editando Parada 2...");
            sistema.editParada(8, "Terminal Norte - Renovada");
            System.out.println("\n>>> Test: Creando Rutas (Conectando el Grafo)...");
            sistema.addRuta(7, 8, 15.5, 12.0, 2.50, 0);
            sistema.addRuta(8, 9, 20.0, 18.5, 3.75, 1);
            System.out.println("\n>>> Test: Editando parámetros de Ruta (1 -> 2)...");
            sistema.editRuta(7, 8, 12.0, 12.0, 2.00, 0);
            System.out.println("\n>>> Test: Eliminando Ruta 2 -> 3...");
            sistema.deleteRuta(7, 8);
            System.out.println("\n>>> Test: Eliminando Parada 3...");
            sistema.deleteParada(9);
            System.out.println("\n=== PRUEBAS FINALIZADAS ===");
*/

/*
        sistema.load_data();
        Map<Integer, Parada> mapa = sistema.getParadaMap();
        if (mapa.size() < 2) {
            System.err.println("Error: Necesitas al menos 2 paradas en la base de datos para buscar una ruta.");
            return;
        }
        Object[] idsDisponibles = mapa.keySet().toArray();
        int idOrigen = (int) idsDisponibles[0];
        int idDestino = (int) idsDisponibles[idsDisponibles.length - 1];
        System.out.println("Origen:  [" + idOrigen + "] " + mapa.get(idOrigen).getNombre());
        System.out.println("Destino: [" + idDestino + "] " + mapa.get(idDestino).getNombre());
        sistema.editRuta(7, 9, 12.5, 19.0, 7.50, 0);
        String[] criterios = {"tiempo", "distancia", "costo"};
        for (String criterio : criterios) {
            List<Parada> camino = Dijkstra.dijkstra(sistema, idOrigen, idDestino, criterio);
            if (camino != null && !camino.isEmpty()) {
                for (int i = 0; i < camino.size(); i++) {
                    System.out.print(camino.get(i).getNombre());
                    if (i < camino.size() - 1) System.out.print(" -> ");
                }
                double total = sistema.calcularTotalRuta(camino, criterio);
                System.out.println(" | " + total);
            } else {
                System.err.println("No se encontró conexión para este criterio.");
            }
        }
*/

/*
        Transporte t = Transporte.getInstancia();
        List<Parada> rutaRapida    = Dijkstra.dijkstra(t, "A", "F", "tiempo");
        List<Parada> rutaCorta     = Dijkstra.dijkstra(t, "A", "F", "distancia");
        List<Parada> rutaEconomica = Dijkstra.dijkstra(t, "A", "F", "costo");
        List<Parada> rutaTrasbordo = Dijkstra.dijkstra(t, "A", "F", "trasbordo");
        rutaRapida.forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println("| " + t.calcularTotalRuta(rutaRapida, "tiempo") + " min");
        rutaCorta.forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println("| " + t.calcularTotalRuta(rutaCorta, "distancia") + " km");
        rutaEconomica.forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println("| $" + t.calcularTotalRuta(rutaEconomica, "costo"));
        rutaTrasbordo.forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println("| " + (int) t.calcularTotalRuta(rutaTrasbordo, "trasbordo") + " trasbordos");
        BellmanFord.bellmanFord(t, "A", "F", "costo").forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println();
        FloydWarshall.floydWarshall(t, "A", "F", "tiempo").forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println();
        BFS.bfs(t, 1).forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println();
        DFS.dfs(t, 2).forEach(p -> System.out.print(p.getNombre() + " -> ")); System.out.println();
*/

        Map<Integer, Parada> mapa = sistema.getParadaMap();
        if (mapa.size() < 2) {
            System.err.println("Error: Necesitas al menos 2 paradas en la base de datos.");
            return;
        }

        Object[] idsDisponibles = mapa.keySet().toArray();
        int idOrigen = (int) idsDisponibles[0];
        int idDestino = (int) idsDisponibles[idsDisponibles.length - 1];

        List<Parada> principal   = Dijkstra.dijkstra(sistema, idOrigen, idDestino, "tiempo");
        List<Parada> alternativa = Dijkstra.dijkstra(sistema, idOrigen, idDestino, "distancia");

        principal.forEach(p -> System.out.print(p.getNombre() + " -> "));
        System.out.println();
        alternativa.forEach(p -> System.out.print(p.getNombre() + " -> "));
        System.out.println();
    }
}
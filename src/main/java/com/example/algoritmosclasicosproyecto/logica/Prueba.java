package com.example.algoritmosclasicosproyecto.logica;

import com.example.algoritmosclasicosproyecto.algoritmos.Dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Prueba {
    public static void main(String[] args) {

        Transporte sistema = new Transporte();

        sistema.getParadaMap().put(1, new Parada(1, "Monumento", 0, 0));
        sistema.getParadaMap().put(2, new Parada(2, "Centro León", 0, 0));
        sistema.getParadaMap().put(3, new Parada(3, "PUCMM", 0, 0));
        sistema.getParadaMap().put(4, new Parada(4, "Las Colinas", 0, 0));
        sistema.getParadaMap().put(5, new Parada(5, "Los Jardines", 0, 0));

        sistema.getListaRuta().put(1, new ArrayList<>());
        sistema.getListaRuta().put(2, new ArrayList<>());
        sistema.getListaRuta().put(3, new ArrayList<>());
        sistema.getListaRuta().put(4, new ArrayList<>());
        sistema.getListaRuta().put(5, new ArrayList<>());

        sistema.addRutaTemporal(1, 2, 20, 2.5, 25, 0);
        sistema.addRutaTemporal(1, 3,  8, 5.0, 20, 0);
        sistema.addRutaTemporal(2, 4,  5, 4.0, 90, 1);
        sistema.addRutaTemporal(3, 4,  6, 2.0, 15, 0);
        sistema.addRutaTemporal(3, 5, 20, 8.0,  8, 0);
        sistema.addRutaTemporal(4, 5, 10, 3.0, 55, 0);

/*
            sistema.addParada( "Estación Central");
            sistema.addParada("Terminal Norte");
            sistema.addParada( "Puerto Madero");
            sistema.editParada(8, "Terminal Norte - Renovada");
            sistema.addRuta(7, 8, 15.5, 12.0, 2.50, 0);
            sistema.addRuta(8, 9, 20.0, 18.5, 3.75, 1);
            sistema.editRuta(7, 8, 12.0, 12.0, 2.00, 0);
            sistema.deleteRuta(7, 8);
            sistema.deleteParada(9);
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

        List<Parada> principal   = Dijkstra.dijkstra(sistema, 1, 5, "tiempo");
        List<Parada> alternativa = Dijkstra.dijkstra(sistema, 1, 5, "distancia");

        principal.forEach(p -> System.out.print(p.getNombre() + " -> "));
        System.out.println();
        alternativa.forEach(p -> System.out.print(p.getNombre() + " -> "));
        System.out.println();
    }
}
package com.example.algoritmosclasicosproyecto.algoritmos;

import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Transporte;
import java.util.*;

public class Dijkstra {

    /**
     * Función: NodoDistancia
     * Objetivo: Clase interna que agrupa el ID de una parada con su distancia acumulada.
     *           Implementa Comparable para que la PriorityQueue ordene automaticamente
     *           de menor a mayor distancia siempre procesando primero la parada más barata
     */

    static class NodoDistancia implements Comparable<NodoDistancia> {
        int id;
        double distancia;

        NodoDistancia(int id, double distancia) {
            this.id = id;
            this.distancia = distancia;
        }

        /**
         * Función: compareTo
         * Argumento: NodoDistancia otro: el nodo con el que se compara
         * Objetivo: Definir el criterio de ordenamiento de la PriorityQueue,
         *           el nodo con menor distancia siempre sale primero
         * Retorno: (int) negativo si este nodo es menor, positivo si es mayor, 0 si son iguales
         */
        @Override
        public int compareTo(NodoDistancia otro) {
            return Double.compare(this.distancia, otro.distancia);
        }
    }
    /**
     * Función: dijkstra
     * Argumento: Transporte transporte: instancia del grafo con todas las paradas y rutas,
     *            int id_Origin: ID de la parada de inicio,
     *            int id_Destination: ID de la parada de destino,
     *            String criterio: criterio de optimización ("tiempo", "distancia", "costo" o "trasbordo")
     * Objetivo: Encontrar la ruta más corta entre dos paradas según el criterio indicado,
     *           usando una PriorityQueue para procesar siempre la parada de menor costo acumulado
     * Retorno: (List<Parada>) Lista ordenada de paradas que forman la ruta más corta.
     *          Retorna null si alguna parada no existe o no hay camino posible.
     */

    public static List<Parada> dijkstra(Transporte transporte, int id_Origin, int id_Destination, String criterio) {
        Map<Integer, Parada> paradaMap = transporte.getParadaMap();
        Map<Integer, List<Ruta>> listaRuta = transporte.getListaRuta();

        if (!paradaMap.containsKey(id_Origin) || !paradaMap.containsKey(id_Destination)) {
            System.err.println("Error: El origen o destino no existe.");
            return null;
        }

        Map<Integer, Double> distancias = new HashMap<>();
        Map<Integer, Integer> anteriores = new HashMap<>();

        for (Integer id : paradaMap.keySet()) {
            distancias.put(id, Double.MAX_VALUE);
        }
        distancias.put(id_Origin, 0.0);

        PriorityQueue<NodoDistancia> pq = new PriorityQueue<>();
        pq.add(new NodoDistancia(id_Origin, 0.0));

        while (!pq.isEmpty()) {
            NodoDistancia actual = pq.poll();
            int idActual = actual.id;

            if (idActual == id_Destination) break;

            if (actual.distancia > distancias.get(idActual)) continue;

            List<Ruta> rutasVecinas = listaRuta.getOrDefault(idActual, new ArrayList<>());
            for (Ruta ruta : rutasVecinas) {
                int vecinoId = ruta.getDestino().getId();
                double nuevaDistancia = distancias.get(idActual) + ruta.getPeso(criterio);
                if (nuevaDistancia < distancias.get(vecinoId)) {
                    distancias.put(vecinoId, nuevaDistancia);
                    anteriores.put(vecinoId, idActual);
                    pq.add(new NodoDistancia(vecinoId, nuevaDistancia));
                }
            }
        }

        if (distancias.get(id_Destination) == Double.MAX_VALUE) {
            System.err.println("No existe camino posible entre " + id_Origin + " y " + id_Destination);
            return null;
        }


        List<Parada> camino = new ArrayList<>();
        Integer paso = id_Destination;
        while (paso != null) {
            camino.add(0, paradaMap.get(paso));
            paso = anteriores.get(paso);
        }

        return camino;
    }
}
package com.example.algoritmosclasicosproyecto.algoritmos;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloydWarshall {

    /**
     * Función: floydWarshall
     * Argumento: Transporte transporte: instancia del grafo con todas las paradas y rutas,
     *            int id_Origin: ID de la parada de inicio,
     *            int id_Destination: ID de la parada de destino,
     *            String criterio: criterio de optimización ("tiempo", "distancia", "costo" o "trasbordo")
     * Objetivo: Calcular la ruta más corta entre todos los pares de paradas del mapa, no solo para una. A diferencia de Dijkstra, procesa todas las rutas
     *           del grafo simultáneamente usando tres for anidados, probando cada parada
     *           como posible punto de paso para encontrar el camino más barato
     * Retorno: (List<Parada>) Lista ordenada de paradas que forman la ruta más corta.
     *          Retorna null si no hay camino posible entre las dos paradas.
     */

    public static List<Parada> floydWarshall(Transporte transporte, int id_Origin, int id_Destination, String criterio) {
        Map<Integer, Parada> paradaMap = transporte.getParadaMap();
        Map<Integer, List<Ruta>> listaRuta = transporte.getListaRuta();
        List<Integer> ids = new ArrayList<>(paradaMap.keySet());

        Map<Integer, Map<Integer, Double>> distancias = new HashMap<>();
        Map<Integer, Map<Integer, Integer>> siguientes = new HashMap<>();


        for (Integer i : ids) {
            distancias.put(i, new HashMap<>());
            siguientes.put(i, new HashMap<>());
            for (Integer j : ids) {

                distancias.get(i).put(j, i.equals(j) ? 0.0 : Double.MAX_VALUE);
                siguientes.get(i).put(j, null);
            }
        }

        for (Integer origen : listaRuta.keySet()) {
            for (Ruta ruta : listaRuta.get(origen)) {
                int destino = ruta.getDestino().getId();
                distancias.get(origen).put(destino, ruta.getPeso(criterio));
                siguientes.get(origen).put(destino, destino);
            }
        }

       for (Integer intermedia : ids) {
            for (Integer origen : ids) {
                for (Integer destino : ids) {
                    double porIntermedia = distancias.get(origen).get(intermedia)
                            + distancias.get(intermedia).get(destino);


                    if (porIntermedia < distancias.get(origen).get(destino)) {
                        distancias.get(origen).put(destino, porIntermedia);
                        siguientes.get(origen).put(destino, siguientes.get(origen).get(intermedia));
                    }
                }
            }
        }

        if (siguientes.get(id_Origin) == null || siguientes.get(id_Origin).get(id_Destination) == null) {
            System.err.println("No existe camino entre " + id_Origin + " y " + id_Destination);
            return null;
        }

        List<Parada> camino = new ArrayList<>();


        int actual = id_Origin;
        while (actual != id_Destination) {
            camino.add(paradaMap.get(actual));
            actual = siguientes.get(actual).get(id_Destination);
        }
        camino.add(paradaMap.get(id_Destination));

        return camino;
    }
}
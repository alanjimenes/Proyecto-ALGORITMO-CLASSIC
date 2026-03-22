package com.example.algoritmosclasicosproyecto.algoritmos;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Transporte;


import java.util.*;
public class Dijkstra {

    static class NodoDistancia implements Comparable<NodoDistancia> {
        String id;
        double distancia;

        NodoDistancia(String id, double distancia) {
            this.id = id;
            this.distancia = distancia;
        }

        @Override
        public int compareTo(NodoDistancia otro) {
            return Double.compare(this.distancia, otro.distancia);
        }
    }

    public static List<Parada> dijkstra(Transporte transporte, String id_Origin, String id_Destination, String criterio) {
        Map<String, Parada> paradaMap = transporte.getParadaMap();
        Map<String, List<Ruta>> listaRuta = transporte.getListaRuta();

        if (!paradaMap.containsKey(id_Origin) || !paradaMap.containsKey(id_Destination)) {
            System.err.println("Error: El origen o destino no existe.");
            return null;
        }

        Map<String, Double> distancias = new HashMap<>();
        Map<String, String> anteriores = new HashMap<>();

        for (String id : paradaMap.keySet()) {
            distancias.put(id, Double.MAX_VALUE);
        }
        distancias.put(id_Origin, 0.0);

        PriorityQueue<NodoDistancia> pq = new PriorityQueue<>();
        pq.add(new NodoDistancia(id_Origin, 0.0));

        while (!pq.isEmpty()) {
            NodoDistancia actual = pq.poll();
            String idActual = actual.id;

            if (idActual.equals(id_Destination)) break;
            if (actual.distancia > distancias.get(idActual)) continue;

            for (Ruta ruta : listaRuta.getOrDefault(idActual, new ArrayList<>())) {
                String vecinoId = ruta.getDestino().getId();
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
        String paso = id_Destination;
        while (paso != null) {
            camino.add(0, paradaMap.get(paso));
            paso = anteriores.get(paso);
        }

        return camino;
    }
}
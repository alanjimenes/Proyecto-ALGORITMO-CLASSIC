package com.example.algoritmosclasicosproyecto.algoritmos;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Transporte;


import java.util.*;
public class Dijkstra {
    private static class NodoDistancia implements Comparable<NodoDistancia> {
        String idParada;
        double distancia;

        public NodoDistancia(String idParada, double distancia) {
            this.idParada = idParada;
            this.distancia = distancia;
        }

        @Override
        public int compareTo(NodoDistancia otro) {
            return Double.compare(this.distancia, otro.distancia);
        }
    }

    private static double WeightRuta(Ruta ruta, String criterio) {
        switch (criterio.toLowerCase()) {
            case "tiempo": return ruta.getTiempo();
            case "distancia": return ruta.getDistancia();
            case "trasbordo": return ruta.getTrasbordo();
            case "costo": return ruta.getCosto();
            default: return ruta.getTiempo();
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
        PriorityQueue<NodoDistancia> pq = new PriorityQueue<>();

        for (String id : paradaMap.keySet()) {
            distancias.put(id, Double.MAX_VALUE);
        }
        distancias.put(id_Origin, 0.0);
        pq.offer(new NodoDistancia(id_Origin, 0.0));
        boolean destino = false;

        while (!pq.isEmpty() && !destino) {
            NodoDistancia actual = pq.poll();
            String idActual = actual.idParada;

            if (idActual.equals(id_Destination)) {
                destino = true;
            } else if (actual.distancia <= distancias.get(idActual)) {
                for (Ruta ruta : listaAdyacencia.getOrDefault(idActual, new ArrayList<>())) {
                    String vecinoId = ruta.getDestino().getId();
                    double peso = WeightRuta(ruta, criterio);
                    double nuevaDistancia = distancias.get(idActual) + peso;

                    if (nuevaDistancia < distancias.get(vecinoId)) {
                        distancias.put(vecinoId, nuevaDistancia);
                        anteriores.put(vecinoId, idActual);
                        pq.offer(new NodoDistancia(vecinoId, nuevaDistancia));
                    }
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

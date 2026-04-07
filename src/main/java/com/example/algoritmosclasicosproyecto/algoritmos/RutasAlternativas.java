package com.example.algoritmosclasicosproyecto.algoritmos;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;

import java.util.*;

public class RutasAlternativas {

    private static final List<String> CRITERIOS = Arrays.asList("tiempo", "distancia", "costo", "trasbordo");

    public static Map<String, List<List<Parada>>> getRutas(Transporte transporte, int id_Origin, int id_Destination) {
        Map<String, List<List<Parada>>> resultado = new LinkedHashMap<>();

        for (String criterio : CRITERIOS) {
            List<List<Parada>> opciones = new ArrayList<>();

            List<Parada> rutaPrincipal = Dijkstra.dijkstra(transporte, id_Origin, id_Destination, criterio);
            if (rutaPrincipal == null) continue;
            opciones.add(rutaPrincipal);


            List<Parada> rutaAlternativa = encontrarAlternativa(transporte, id_Origin, id_Destination, criterio, rutaPrincipal);
            if (rutaAlternativa != null) {
                opciones.add(rutaAlternativa);
            }

            resultado.put(criterio, opciones);
        }

        return resultado;
    }

    private static List<Parada> encontrarAlternativa(Transporte transporte, int id_Origin, int id_Destination, String criterio, List<Parada> rutaPrincipal) {
        List<Parada> mejorAlternativa = null;
        double mejorPeso = Double.MAX_VALUE;


        for (int i = 0; i < rutaPrincipal.size() - 1; i++) {
            int ignorarOrigen = rutaPrincipal.get(i).getId();
            int ignorarDestino = rutaPrincipal.get(i + 1).getId();


            List<Parada> alternativa = miniDijkstra(transporte, id_Origin, id_Destination, criterio, ignorarOrigen, ignorarDestino);

            if (alternativa != null && !alternativa.equals(rutaPrincipal)) {
                double pesoAlternativa = calcularPesoRuta(alternativa, transporte, criterio);
                if (pesoAlternativa < mejorPeso) {
                    mejorPeso = pesoAlternativa;
                    mejorAlternativa = alternativa;
                }
            }
        }

        return mejorAlternativa;
    }




    private static List<Parada> miniDijkstra(Transporte transporte, int origen, int destino, String criterio, int ignorarOri, int ignorarDest) {
        Map<Integer, Double> distancias = new HashMap<>();
        Map<Integer, Integer> previos = new HashMap<>();
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));

        for (Parada p : transporte.getParadas()) {
            distancias.put(p.getId(), Double.MAX_VALUE);
        }
        distancias.put(origen, 0.0);
        pq.add(origen);

        while (!pq.isEmpty()) {
            int actual = pq.poll();

            if (actual == destino) break;

            for (Ruta r : transporte.getListaRuta().getOrDefault(actual, new ArrayList<>())) {
                int vecino = r.getDestino().getId();


                if (actual == ignorarOri && vecino == ignorarDest) continue;

                double nuevoPeso = distancias.get(actual) + r.getPeso(criterio);
                if (nuevoPeso < distancias.get(vecino)) {
                    distancias.put(vecino, nuevoPeso);
                    previos.put(vecino, actual);
                    pq.add(vecino);
                }
            }
        }

        if (!previos.containsKey(destino)) return null;

        List<Parada> camino = new ArrayList<>();
        Integer paso = destino;
        while (paso != null) {
            camino.add(0, transporte.getParadaMap().get(paso));
            paso = previos.get(paso);
        }
        return camino;
    }

    private static double calcularPesoRuta(List<Parada> ruta, Transporte transporte, String criterio) {
        double total = 0.0;
        for (int i = 0; i < ruta.size() - 1; i++) {
            Ruta r = transporte.getRuta(ruta.get(i).getId(), ruta.get(i + 1).getId());
            if (r != null) {
                total += r.getPeso(criterio);
            }
        }
        return total;
    }
}
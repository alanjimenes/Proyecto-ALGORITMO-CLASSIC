package com.example.algoritmosclasicosproyecto.algoritmos;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BellmanFord {

    public static List<Parada> bellmanFord(Transporte transporte, String id_Origin, String id_Destination, String criterio) {
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

        int V = paradaMap.size();


        for (int i = 0; i < V - 1; i++) {

            for (String actual : listaRuta.keySet()) {
                if (distancias.get(actual) == Double.MAX_VALUE) continue;

                for (Ruta ruta : listaRuta.get(actual)) {
                    String vecinoId = ruta.getDestino().getId();
                    double peso = getPeso(ruta, criterio);
                    double nuevaDistancia = distancias.get(actual) + peso;

                    if (nuevaDistancia < distancias.get(vecinoId)) {
                        distancias.put(vecinoId, nuevaDistancia);
                        anteriores.put(vecinoId, actual);
                    }
                }
            }
        }

        if (distancias.get(id_Destination) == Double.MAX_VALUE) {
            System.err.println("No existe camino entre " + id_Origin + " y " + id_Destination);
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

    private static double getPeso(Ruta ruta, String criterio) {
        switch (criterio.toLowerCase()) {
            case "tiempo":     return ruta.getTiempo();
            case "distancia":  return ruta.getDistancia();
            case "costo":      return ruta.getCosto();
            case "trasbordo":  return ruta.getTrasbordo();
            default:           return ruta.getTiempo();
        }
    }
}
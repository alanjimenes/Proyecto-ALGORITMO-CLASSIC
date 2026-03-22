package com.example.algoritmosclasicosproyecto.logica;

import com.example.algoritmosclasicosproyecto.algoritmos.Dijkstra;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RutasAlternativas {

    private static final List<String> CRITERIOS = Arrays.asList("tiempo", "distancia", "costo", "trasbordo");

    public static Map<String, List<Parada>> getRutas(Transporte transporte, String id_Origin, String id_Destination) {
        Map<String, List<Parada>> rutas = new LinkedHashMap<>();

        for (String criterio : CRITERIOS) {
            List<Parada> ruta = Dijkstra.dijkstra(transporte, id_Origin, id_Destination, criterio);
            if (ruta != null) {
                rutas.put(criterio, ruta);
            }
        }

        return rutas;
    }
}
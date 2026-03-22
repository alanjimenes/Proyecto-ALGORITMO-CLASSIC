package com.example.algoritmosclasicosproyecto.algoritmos;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloydWarshall {

    public static List<Parada> floydWarshall(Transporte transporte, String id_Origin, String id_Destination, String criterio) {
        Map<String, Parada> paradaMap = transporte.getParadaMap();
        Map<String, List<Ruta>> listaRuta = transporte.getListaRuta();
        List<String> ids = new ArrayList<>(paradaMap.keySet());

        Map<String, Map<String, Double>> distancias = new HashMap<>();
        Map<String, Map<String, String>> siguientes = new HashMap<>();

        // Paso 1: todas las distancias empiezan en infinito
        for (String i : ids) {
            distancias.put(i, new HashMap<>());
            siguientes.put(i, new HashMap<>());
            for (String j : ids) {
                distancias.get(i).put(j, i.equals(j) ? 0.0 : Double.MAX_VALUE);
                siguientes.get(i).put(j, null);
            }
        }

        // Paso 2: cargar las rutas que ya existen
        for (String origen : listaRuta.keySet()) {
            for (Ruta ruta : listaRuta.get(origen)) {
                String destino = ruta.getDestino().getId();
                distancias.get(origen).put(destino, ruta.getPeso(criterio));
                siguientes.get(origen).put(destino, destino);
            }
        }

        // Paso 3: probar cada parada como intermedia
        for (String intermedia : ids) {
            for (String origen : ids) {
                for (String destino : ids) {
                    double porIntermedia = distancias.get(origen).get(intermedia)
                            + distancias.get(intermedia).get(destino);

                    // ¿Es más corto pasar por la intermedia?
                    if (porIntermedia < distancias.get(origen).get(destino)) {
                        distancias.get(origen).put(destino, porIntermedia);
                        siguientes.get(origen).put(destino, siguientes.get(origen).get(intermedia));
                    }
                }
            }
        }

        // Paso 4: reconstruir el camino
        if (siguientes.get(id_Origin).get(id_Destination) == null) {
            System.err.println("No existe camino entre " + id_Origin + " y " + id_Destination);
            return null;
        }

        List<Parada> camino = new ArrayList<>();
        String actual = id_Origin;
        while (!actual.equals(id_Destination)) {
            camino.add(paradaMap.get(actual));
            actual = siguientes.get(actual).get(id_Destination);
        }
        camino.add(paradaMap.get(id_Destination));

        return camino;
    }
}
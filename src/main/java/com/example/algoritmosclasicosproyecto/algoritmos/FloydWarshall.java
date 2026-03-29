package com.example.algoritmosclasicosproyecto.algoritmos;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloydWarshall {

    public static List<Parada> floydWarshall(Transporte transporte, int id_Origin, int id_Destination, String criterio) {
        Map<Integer, Parada> paradaMap = transporte.getParadaMap();
        Map<Integer, List<Ruta>> listaRuta = transporte.getListaRuta();
        List<Integer> ids = new ArrayList<>(paradaMap.keySet());

        Map<Integer, Map<Integer, Double>> distancias = new HashMap<>();
        Map<Integer, Map<Integer, Integer>> siguientes = new HashMap<>();

        // Paso 1: todas las distancias empiezan en infinito
        for (Integer i : ids) {
            distancias.put(i, new HashMap<>());
            siguientes.put(i, new HashMap<>());
            for (Integer j : ids) {

                distancias.get(i).put(j, i.equals(j) ? 0.0 : Double.MAX_VALUE);
                siguientes.get(i).put(j, null);
            }
        }

        // Paso 2: cargar las rutas que ya existen
        for (Integer origen : listaRuta.keySet()) {
            for (Ruta ruta : listaRuta.get(origen)) {
                int destino = ruta.getDestino().getId();
                distancias.get(origen).put(destino, ruta.getPeso(criterio));
                siguientes.get(origen).put(destino, destino);
            }
        }

        // Paso 3: probar cada parada como intermedia
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

        // Paso 4: reconstruir el camino
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
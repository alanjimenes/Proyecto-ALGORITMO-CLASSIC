package com.example.algoritmosclasicosproyecto.algoritmos;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;

import java.util.*;

public class DFS {

    public static List<Parada> dfs(Transporte transporte, String id_Origin) {
        Map<String, Parada> paradaMap = transporte.getParadaMap();
        Map<String, List<Ruta>> listaRuta = transporte.getListaRuta();

        if (!paradaMap.containsKey(id_Origin)) {
            System.err.println("Error: La parada origen no existe.");
            return null;
        }

        List<Parada> visitados = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        dfsRecursivo(id_Origin, paradaMap, listaRuta, visited, visitados);

        return visitados;
    }

    private static void dfsRecursivo(String actual, Map<String, Parada> paradaMap,
                                     Map<String, List<Ruta>> listaRuta,
                                     Set<String> visited, List<Parada> visitados) {
        visited.add(actual);
        visitados.add(paradaMap.get(actual));

        for (Ruta ruta : listaRuta.getOrDefault(actual, new ArrayList<>())) {
            String vecinoId = ruta.getDestino().getId();
            if (!visited.contains(vecinoId)) {
                dfsRecursivo(vecinoId, paradaMap, listaRuta, visited, visitados);
            }
        }
    }
}
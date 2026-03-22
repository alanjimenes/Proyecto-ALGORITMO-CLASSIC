package com.example.algoritmosclasicosproyecto.algoritmos;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;

import java.util.*;

public class BFS {

    public static List<Parada> bfs(Transporte transporte, String id_Origin) {
        Map<String, Parada> paradaMap = transporte.getParadaMap();
        Map<String, List<Ruta>> listaRuta = transporte.getListaRuta();

        if (!paradaMap.containsKey(id_Origin)) {
            System.err.println("Error: La parada origen no existe.");
            return null;
        }

        List<Parada> visitados = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        visited.add(id_Origin);
        queue.add(id_Origin);

        while (!queue.isEmpty()) {
            String actual = queue.poll();
            visitados.add(paradaMap.get(actual));

            for (Ruta ruta : listaRuta.getOrDefault(actual, new ArrayList<>())) {
                String vecinoId = ruta.getDestino().getId();
                if (!visited.contains(vecinoId)) {
                    visited.add(vecinoId);
                    queue.add(vecinoId);
                }
            }
        }

        return visitados;
    }
}
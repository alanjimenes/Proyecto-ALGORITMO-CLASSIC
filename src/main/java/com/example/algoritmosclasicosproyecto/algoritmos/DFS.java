package com.example.algoritmosclasicosproyecto.algoritmos;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;

import java.util.*;

public class DFS {

    public static List<Parada> dfs(Transporte transporte, int id_Origin) {
        Map<Integer, Parada> paradaMap = transporte.getParadaMap();
        Map<Integer, List<Ruta>> listaRuta = transporte.getListaRuta();

        if (!paradaMap.containsKey(id_Origin)) {
            System.err.println("Error: La parada origen con ID " + id_Origin + " no existe.");
            return null;
        }


        List<Parada> orderNeighbor = new ArrayList<>();
        Set<Integer> visitados = new HashSet<>();

        dfsRecursivo(id_Origin, paradaMap, listaRuta, visitados, orderNeighbor);

        return orderNeighbor;
    }

    private static void dfsRecursivo(int actualId,  Map<Integer, Parada> paradaMap, Map<Integer, List<Ruta>> listaRuta, Set<Integer> visitados, List<Parada> orden_Visita) {
        visitados.add(actualId);
        orden_Visita.add(paradaMap.get(actualId));

        List<Ruta> rutasVecinas = listaRuta.getOrDefault(actualId, new ArrayList<>());
        for (Ruta ruta : rutasVecinas) {
            int vecinoId = ruta.getDestino().getId();

            if (!visitados.contains(vecinoId)) {
                dfsRecursivo(vecinoId, paradaMap, listaRuta, visitados, orden_Visita);
            }
        }
    }
}
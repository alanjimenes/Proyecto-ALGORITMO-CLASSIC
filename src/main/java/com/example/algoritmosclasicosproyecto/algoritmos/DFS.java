package com.example.algoritmosclasicosproyecto.algoritmos;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;

import java.util.*;


public class DFS {

    /**
     * Función: dfs (Búsqueda en Profundidad)
     * Argumento: int actualId: ID de la parada que se está procesando en esta llamada,
     *            Map<Integer, Parada> paradaMap: mapa de todas las paradas del sistema,
     *            Map<Integer, List<Ruta>> listaRuta: mapa de todas las conexiones del grafo,
     *            Set<Integer> visitados: conjunto de IDs ya procesados compartido entre llamadas,
     *            List<Parada> orden_Visita: lista acumulada del orden de recorrido
     * Objetivo: Método auxiliar recursivo que profundiza por cada camino del grafo.
     *           El caso base ocurre cuando todos los vecinos del nodo actual ya fueron visitados,
     *           momento en que la función retorna sin hacer más llamadas recursivas
     * Retorno: void — modifica directamente las listas visitados y orden_Visita
     */

    public static List<Parada> dfs(Transporte transporte, int id_Origin) {

        Map<Integer, Parada> paradaMap = transporte.getParadaMap();
        Map<Integer, List<Ruta>> listaRuta = transporte.getListaRuta();


        if (!paradaMap.containsKey(id_Origin)) {
            System.err.println("Error: La parada origen con ID " + id_Origin + " no existe.");
            return null;
        }


        List<Parada> ordenDeVisita = new ArrayList<>();
        Set<Integer> visitados = new HashSet<>();

        dfsRecursivo(id_Origin, paradaMap, listaRuta, visitados, ordenDeVisita);

        return ordenDeVisita;
    }

    private static void dfsRecursivo(int actualId,  Map<Integer, Parada> paradaMap, Map<Integer, List<Ruta>> listaRuta, Set<Integer> visitados, List<Parada> orden_Visita) {
        visitados.add(actualId);
        orden_Visita.add(paradaMap.get(actualId));

        List<Ruta> rutasVecinas = listaRuta.getOrDefault(actualId, new ArrayList<>());
        for (Ruta ruta : rutasVecinas) {
            int vecinoId = ruta.getDestino().getId();

            // Si el vecino no ha sido visitado, profundizar
            if (!visitados.contains(vecinoId)) {
                dfsRecursivo(vecinoId, paradaMap, listaRuta, visitados, orden_Visita);
            }
        }
    }
}
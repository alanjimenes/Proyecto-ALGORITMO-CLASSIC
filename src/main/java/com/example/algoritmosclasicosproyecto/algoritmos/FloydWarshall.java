package com.example.algoritmosclasicosproyecto.algoritmos;
import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;

import java.util.*;


//Complejidad: O(V^3)
public class FloydWarshall {
    //Matrices: floyd busca entre todos los pares de vertices el camino mas corto contando los negativos
    public static List<Parada> calcRuta(Transporte transporte, int idOrigen, int idDestino, String criterio) {
        List<Parada> paradas = transporte.getParadas();
        int n = paradas.size();

        Map<Integer, Integer> indice = new HashMap<>();
        // Convierte los id en indices de una matriz
        for (int i = 0; i < n; i++) {
            indice.put(paradas.get(i).getId(), i);
        }
        //Distancias guarda el costo mas bajo entre cualquier par de
        // paradas y se llena con infinito porque al iniciar no sabemos si existe un camino
        double[][] distancias = new double[n][n];
        Integer[][] siguiente = new Integer[n][n];//rastro de migas de pan esto guarda el sigt paso para llegar de i a j

        for (int i = 0; i < n; i++) {
            Arrays.fill(distancias[i], Double.POSITIVE_INFINITY);
            distancias[i][i] = 0;
        }
        //esto revisa la lista y si existe una ruta directa entre una parada y otra, anota su peso en la matriz
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int u = paradas.get(i).getId();
                int v = paradas.get(j).getId();
                Ruta r = transporte.getConexion(u, v);

                if (r != null && r.isDisponible()) {
                    distancias[i][j] = r.getPeso(criterio);
                    siguiente[i][j] = j;
                }
            }
        }
    //Es mas corto ir de i a j pasando primero por k que ir por el camino que ya conozco
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                        siguiente[i][j] = siguiente[i][k];
                    }
                }
            }
        }


        return buildCamino(idOrigen, idDestino, indice, paradas, siguiente, distancias);
    }


    //complejidad O(V)
    private static List<Parada> buildCamino(int origen, int destino, Map<Integer, Integer> map, List<Parada> paradas, Integer[][] siguiente, double[][] dist) {
        Integer u = map.get(origen);
        Integer v = map.get(destino);

        if (u == null || v == null || dist[u][v] == Double.POSITIVE_INFINITY) return null;
        //Si despues de todo el proceso ejecutado sigue siendo infinito significa que es imposible llegar

        List<Parada> camino = new ArrayList<>();
        camino.add(paradas.get(u));
        //VA AVANZANDO Y SE VA ACTUALIZANDO HASTA FINALMENTE ENCONTRAR UN CAMINO ENTRE U Y V
        while (!u.equals(v)) {
            u = siguiente[u][v];
            if (u == null) return null;
            camino.add(paradas.get(u));
        }
        return camino;
    }
}

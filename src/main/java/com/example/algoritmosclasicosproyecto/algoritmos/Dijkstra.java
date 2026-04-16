package com.example.algoritmosclasicosproyecto.algoritmos;

import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Transporte;
import java.util.*;

public class Dijkstra {


    static class NodoDistancia implements Comparable<NodoDistancia> {
        int idParada;
        double costoAcumulado;

        NodoDistancia(int idParada, double costoAcumulado) {
            this.idParada = idParada;
            this.costoAcumulado = costoAcumulado;
        }

        @Override
        public int compareTo(NodoDistancia otro) {
            return Double.compare(this.costoAcumulado, otro.costoAcumulado);
        }
    }


    public static List<Parada> calcularRuta(Transporte transporte, int idOrigen, int idDestino, String criterio) {

        Map<Integer, Parada> mapaParadas = transporte.getParadaMap();
        Map<Integer, List<Ruta>> mapaRutas = transporte.getListaRuta();

        if (!mapaParadas.containsKey(idOrigen) || !mapaParadas.containsKey(idDestino)) {
            System.err.println("Origen o destino no existe");
            return null;
        }

        Map<Integer, Double> costoMinimo = new HashMap<>();
        Map<Integer, Integer> paradaAnterior = new HashMap<>();


        for (Integer id : mapaParadas.keySet()) {
            costoMinimo.put(id, Double.POSITIVE_INFINITY);
        }

        costoMinimo.put(idOrigen, 0.0);

        PriorityQueue<NodoDistancia> colaPrioridad = new PriorityQueue<>();
        colaPrioridad.add(new NodoDistancia(idOrigen, 0.0));

        while (!colaPrioridad.isEmpty()) {

            NodoDistancia actual = colaPrioridad.poll(); //saca el nodo con costo mas bajo disponible
            int idActual = actual.idParada;

            if (idActual == idDestino) break;
            //si hay un camino mas largo que el que ya tenemos registrado para ese nodo se ignora
            if (actual.costoAcumulado > costoMinimo.get(idActual)) continue;

            List<Ruta> conexiones = mapaRutas.getOrDefault(idActual, new ArrayList<>());
            //Relajacion de aristas
            for (Ruta conexion : conexiones) {
                if (!conexion.isDisponible()) continue;//saltar a la siguiente ruta si esta no esta disponible
                int idVecino = conexion.getDestino().getId();

                double nuevoCosto = costoMinimo.get(idActual) + conexion.getPeso(criterio);

                if (nuevoCosto < costoMinimo.get(idVecino)) {
                    costoMinimo.put(idVecino, nuevoCosto);
                    paradaAnterior.put(idVecino, idActual);
                    colaPrioridad.add(new NodoDistancia(idVecino, nuevoCosto));
                }
            }
        }

        if (costoMinimo.get(idDestino) == Double.POSITIVE_INFINITY) {
            System.err.println("No hay camino");
            return null;
        }

        //Reconstruccion de camino

        List<Parada> rutaFinal = new ArrayList<>();
        Integer nodoActual = idDestino;

        while (nodoActual != null) {
            rutaFinal.add(0, mapaParadas.get(nodoActual));
            nodoActual = paradaAnterior.get(nodoActual);
        }

        return rutaFinal;
    }
}
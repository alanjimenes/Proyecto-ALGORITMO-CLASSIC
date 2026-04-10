package com.example.algoritmosclasicosproyecto.algoritmos;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;

import java.util.*;

public class RutasAlternativas {

    public static List<Parada> calcularAlternativa(
            Transporte transporte,
            int idOrigen,
            int idDestino,
            String criterio) {

        List<Parada> rutaPrincipal = Dijkstra.calcularRuta(
                transporte, idOrigen, idDestino, criterio);

        if (rutaPrincipal == null || rutaPrincipal.size() < 2) return null;

        int idInicio = rutaPrincipal.get(0).getId();
        int idSiguiente = rutaPrincipal.get(1).getId();

        Ruta conexion = transporte.getRuta(idInicio, idSiguiente);
        if (conexion == null) return null;


        conexion.setDisponible(false);

        List<Parada> alternativa = Dijkstra.calcularRuta(
                transporte, idOrigen, idDestino, criterio);


        conexion.setDisponible(true);

        return alternativa;
    }
}
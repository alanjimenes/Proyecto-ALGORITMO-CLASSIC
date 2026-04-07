package com.example.algoritmosclasicosproyecto.algoritmos;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;

import java.util.*;

public class RutasAlternativas {

    private static final List<String> CRITERIOS = Arrays.asList("tiempo", "distancia", "costo", "trasbordo");

    public static Map<String, List<List<Parada>>> getRutas(Transporte transporte, int id_Origin, int id_Destination) {
        Map<String, List<List<Parada>>> resultado = new LinkedHashMap<>();

        for (String criterio : CRITERIOS) {
            List<List<Parada>> opciones = new ArrayList<>();

            List<Parada> rutaPrincipal = Dijkstra.dijkstra(transporte, id_Origin, id_Destination, criterio);
            if (rutaPrincipal == null) continue;
            opciones.add(rutaPrincipal);

            List<Parada> rutaAlternativa = searchAlternativa(transporte, id_Origin, id_Destination, criterio, rutaPrincipal);
            if (rutaAlternativa != null) {
                opciones.add(rutaAlternativa);
            }

            resultado.put(criterio, opciones);
        }

        return resultado;
    }

    private static List<Parada> searchAlternativa(Transporte transporte, int id_Origin, int id_Destination, String criterio, List<Parada> rutaPrincipal) {
        List<Parada> mejorAlternativa = null;
        double mejorPeso = Double.MAX_VALUE;

        for (int i = 0; i < rutaPrincipal.size() - 1; i++) {
            int paradaActual = rutaPrincipal.get(i).getId();
            int paradaSiguiente = rutaPrincipal.get(i + 1).getId();
  Ruta conexion = transporte.getRuta(paradaActual, paradaSiguiente);
            if (conexion == null) continue;

            transporte.deleteRutaTemporal(paradaActual, paradaSiguiente);
            List<Parada> alternativa = Dijkstra.dijkstra(transporte, id_Origin, id_Destination, criterio);
            transporte.addRutaTemporal(paradaActual, paradaSiguiente,
                    conexion.getTiempo(), conexion.getDistancia(),
                    conexion.getCosto(), conexion.getTrasbordo());


            if (alternativa != null && !alternativa.equals(rutaPrincipal)) {
                double pesoAlternativa = calcWeightRuta(alternativa, transporte, criterio);
                if (pesoAlternativa < mejorPeso) {
                    mejorPeso = pesoAlternativa;
                    mejorAlternativa = alternativa;
                }
            }
        }

        return mejorAlternativa;
    }

    private static double calcWeightRuta(List<Parada> ruta, Transporte transporte, String criterio) {
        double total = 0.0;
        for (int i = 0; i < ruta.size() - 1; i++) {
            Ruta r = transporte.getRuta(ruta.get(i).getId(), ruta.get(i + 1).getId());
            if (r != null) {
                total += r.getPeso(criterio);
            }
        }
        return total;
    }
}
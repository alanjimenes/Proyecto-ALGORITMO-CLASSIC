package com.example.algoritmosclasicosproyecto.logica;

import com.example.algoritmosclasicosproyecto.algoritmos.Dijkstra;

import java.util.*;

public class RutasAlternativas {

    private static final List<String> CRITERIOS = Arrays.asList("tiempo", "distancia", "costo", "trasbordo");

    public static Map<String, List<List<Parada>>> getRutas(Transporte transporte, int id_Origin, int id_Destination) {
        Map<String, List<List<Parada>>> resultado = new LinkedHashMap<>();

        for (String criterio : CRITERIOS) {
            List<List<Parada>> opciones = new ArrayList<>();

            // Ruta principal (amarilla)
            List<Parada> rutaPrincipal = Dijkstra.dijkstra(transporte, id_Origin, id_Destination, criterio);
            if (rutaPrincipal == null) continue;
            opciones.add(rutaPrincipal);

            // Ruta alternativa (azul)
            List<Parada> rutaAlternativa = encontrarAlternativa(transporte, id_Origin, id_Destination, criterio, rutaPrincipal);
            if (rutaAlternativa != null) {
                opciones.add(rutaAlternativa);
            }

            resultado.put(criterio, opciones);
        }

        return resultado;
    }

    private static List<Parada> encontrarAlternativa(Transporte transporte, int id_Origin, int id_Destination, String criterio, List<Parada> rutaPrincipal) {

        for (int i = 0; i < rutaPrincipal.size() - 1; i++) {
            int paradaActual    = rutaPrincipal.get(i).getId();
            int paradaSiguiente = rutaPrincipal.get(i + 1).getId();

            Ruta conexion = transporte.getRuta(paradaActual, paradaSiguiente);

            transporte.deleteRutaTemporal(paradaActual, paradaSiguiente); // ← cambio

            List<Parada> alternativa = Dijkstra.dijkstra(transporte, id_Origin, id_Destination, criterio);

            if (conexion != null) {
                transporte.addRutaTemporal(paradaActual, paradaSiguiente, // ← cambio
                        conexion.getTiempo(), conexion.getDistancia(),
                        conexion.getCosto(), conexion.getTrasbordo());
            }

            if (alternativa != null && !alternativa.equals(rutaPrincipal)) {
                return alternativa;
            }
        }

        return null;
    }
}
package com.example.algoritmosclasicosproyecto.logica;

import com.example.algoritmosclasicosproyecto.algoritmos.Dijkstra;

import java.util.*;

public class RutasAlternativas {

    private static final List<String> CRITERIOS = Arrays.asList("tiempo", "distancia", "costo", "trasbordo");

    public static Map<String, List<List<Parada>>> getRutas(Transporte transporte, String id_Origin, String id_Destination) {
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

    private static List<Parada> encontrarAlternativa(Transporte transporte, String id_Origin, String id_Destination, String criterio, List<Parada> rutaPrincipal) {

        for (int i = 0; i < rutaPrincipal.size() - 1; i++) {
            String paradaActual  = rutaPrincipal.get(i).getId();
            String paradaSiguiente = rutaPrincipal.get(i + 1).getId();


            Ruta conexion = transporte.getRuta(paradaActual, paradaSiguiente);

            transporte.deleteRuta(paradaActual, paradaSiguiente);

            // Buscar otra ruta sin esa conexión
            List<Parada> alternativa = Dijkstra.dijkstra(transporte, id_Origin, id_Destination, criterio);


            if (conexion != null) {
                transporte.addRuta(paradaActual, paradaSiguiente,
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
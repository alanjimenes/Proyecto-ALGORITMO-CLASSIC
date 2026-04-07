package com.example.algoritmosclasicosproyecto.algoritmos;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;

import java.util.*;

public class RutasAlternativas {

    private static final List<String> CRITERIOS = Arrays.asList("tiempo", "distancia", "costo", "trasbordo");


    /**
     * Función: getRutas
     * Argumento: Transporte transporte: instancia del grafo con todas las paradas y rutas,
     *            int id_Origin: ID de la parada de inicio,
     *            int id_Destination: ID de la parada de destino
     * Objetivo: Calcular para cada criterio disponible una ruta principal y una ruta
     *           alternativa entre dos paradas
     * Retorno: (Map<String, List<List<Parada>>>) Mapa donde cada criterio tiene una lista
     *          con la ruta principal en la posición 0 y la alternativa en la posición 1
     */

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


    /**
     * Función: searchAlternativa
     * Argumento: Transporte transporte: instancia del grafo con todas las paradas y rutas,
     *            int id_Origin: ID de la parada de inicio,
     *            int id_Destination: ID de la parada de destino,
     *            String criterio: criterio de optimización ("tiempo", "distancia", "costo" o "trasbordo"),
     *            List<Parada> rutaPrincipal: la ruta óptima ya calculada que se quiere evitar
     * Objetivo: Encontrar la mejor ruta alternativa eliminando temporalmente una por una
     *           cada conexión de la ruta principal en memoria, forzando a Dijkstra a buscar
     *           otro camino. La conexión se restaura después de cada búsqueda
     * Retorno: (List<Parada>) La mejor ruta alternativa encontrada.
     *          Retorna null si no existe ningún camino alternativo
     */

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

    /**
     * Función: calcWeightRuta
     * Argumento: List<Parada> ruta: lista de paradas que forman el camino,
     *            Transporte transporte: instancia del grafo con todas las paradas y rutas,
     *            String criterio: criterio de optimización
     * Objetivo: Calcular el costo total de una ruta sumando el peso de cada conexión
     *           según el criterio indicado, usado para comparar alternativas entre sí
     * Retorno: (double) Costo total acumulado de la ruta
     */

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
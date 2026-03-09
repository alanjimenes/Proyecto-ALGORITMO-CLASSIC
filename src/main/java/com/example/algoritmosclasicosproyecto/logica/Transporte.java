package com.example.algoritmosclasicosproyecto.logica;

import java.util.HashMap;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transporte {
    private Map<String, Parada> paradaMap;
    private Map<String, List<Ruta>> listaAdyacencia;

    public Transporte() {
        this.paradaMap = new HashMap<>();
        this.listaAdyacencia = new HashMap<>();
    }

    public void addParada(String id, String nombre) {
        if (!paradaMap.containsKey(id)) {
            String sql = "insert into parada values (?,?)";

            try (Connection conn = Conexion.conectar();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                pstmt.setString(2, nombre);
                pstmt.executeUpdate();

                Parada new_parada = new Parada(id, nombre);
                paradaMap.put(id, new_parada);
                listaAdyacencia.put(id, new ArrayList<>());

                System.out.println("Parada agregada a BD y memoria: " + nombre);

            } catch (SQLException e) {
                System.err.println("Error al insertar parada en la BD: " + e.getMessage());
            }
        } else {
            System.err.println("La parada con ID " + id + " ya existe en memoria.");
        }
    }

    public void editParada(String id, String nombre) {
        if (paradaMap.containsKey(id)) {
            Parada p = paradaMap.get(id);
            p.setNombre(nombre);
        } else {
            System.out.println("Error: La parada con ID " + id + " no existe.");
        }
    }

    public void deleteParada(String id) {
        if (paradaMap.containsKey(id)) {
            paradaMap.remove(id);
            listaAdyacencia.remove(id);
            for (List<Ruta> rutas : listaAdyacencia.values()) {
                rutas.removeIf(ruta -> ruta.getDestino().getId().equals(id));
            }
        }
    }

    public void addRuta(String id_Origin, String id_Destination, double tiempo, double distancia, double costo, boolean trasbordo) {
        if (!paradaMap.containsKey(id_Origin) || !paradaMap.containsKey(id_Destination)) {
            System.err.println("Error: El origen o destino no existe.");
            return;
        }

        if (tiempo < 0 || distancia < 0 || costo < 0) {
            System.err.println("Error: Tiempo, distancia y costo no pueden ser negativos.");
            return;
        }

        for (Ruta r : listaAdyacencia.get(id_Origin)) {
            if (r.getDestino().getId().equals(id_Destination)) {
                System.err.println("Error: Ya existe una ruta de " + id_Origin + " a " + id_Destination);
                return;
            }
        }

        Parada destino = paradaMap.get(id_Destination);
        listaAdyacencia.get(id_Origin).add(new Ruta(destino, tiempo, distancia, costo, trasbordo));
    }

    public void deleteRuta(String id_Origin, String id_Destination) {
        if (listaAdyacencia.containsKey(id_Origin)) {
            List<Ruta> rutas = listaAdyacencia.get(id_Origin);
            rutas.removeIf(ruta -> ruta.getDestino().getId().equals(id_Destination));
        }
    }

    public void editRuta(String id_origin, String id_destination, double tiempo, double distancia, double costo, boolean trasbordo) {

        if (!paradaMap.containsKey(id_origin) || !paradaMap.containsKey(id_destination)) {
            System.err.println("Error: El origen o destino no existe.");
            return;
        }

        if (tiempo < 0 || distancia < 0 || costo < 0) {
            System.err.println("Error: Tiempo, distancia y costo no pueden ser negativos.");
            return;
        }

        List<Ruta> rutas = listaAdyacencia.get(id_origin);
        boolean encontrada = false;
        int i = 0;
        while (i < rutas.size() && !encontrada) {
            Ruta r = rutas.get(i);
            if (r.getDestino().getId().equals(id_destination)) {
                r.setTiempoMinuto(tiempo);
                r.setDistanciaKm(distancia);
                r.setCosto(costo);
                r.setRequiereTrasbordo(trasbordo);
                encontrada = true;
            }
            i++;
        }

        if (!encontrada) {
            System.err.println("Error: La ruta de " + id_origin + " a " + id_destination + " no existe.");
        }
    }

    
    public List<Parada> dijkstra(String id_Origin, String id_Destination, String criterio) {
        if (!paradaMap.containsKey(id_Origin) || !paradaMap.containsKey(id_Destination)) {
            System.err.println("Error: El origen o destino no existe.");
            return null;
        }

        Map<String, Double> distancias = new HashMap<>();
        Map<String, String> anteriores = new HashMap<>();

        for (String id : paradaMap.keySet()) {
            distancias.put(id, Double.MAX_VALUE);
        }
        distancias.put(id_Origin, 0.0);

        List<String> noVisitados = new ArrayList<>(paradaMap.keySet());

        while (!noVisitados.isEmpty()) {
            String actual = null;
            for (String id : noVisitados) {
                if (actual == null || distancias.get(id) < distancias.get(actual)) {
                    actual = id;
                }
            }

            if (actual.equals(id_Destination)) break;
            if (distancias.get(actual) == Double.MAX_VALUE) break;

            noVisitados.remove(actual);

            for (Ruta ruta : listaAdyacencia.get(actual)) {
                String vecinoId = ruta.getDestino().getId();

                double peso;
                switch (criterio) {
                    case "tiempo":    peso = ruta.getTiempoMinuto(); break;
                    case "distancia": peso = ruta.getDistanciaKm();  break;
                    default:          peso = ruta.getCosto();         break;
                }

                double nuevaDistancia = distancias.get(actual) + peso;

                if (nuevaDistancia < distancias.get(vecinoId)) {
                    distancias.put(vecinoId, nuevaDistancia);
                    anteriores.put(vecinoId, actual);
                }
            }
        }

        List<Parada> camino = new ArrayList<>();
        String paso = id_Destination;

        while (paso != null) {
            camino.add(0, paradaMap.get(paso));
            paso = anteriores.get(paso);
        }

        if (camino.isEmpty() || !camino.get(0).getId().equals(id_Origin)) {
            System.err.println("No existe camino entre " + id_Origin + " y " + id_Destination);
            return null;
        }

        return camino;
    }

    public void cargarDatosPrueba() {
        addParada("A", "Monumento");
        addParada("B", "Centro León");
        addParada("C", "PUCMM");
        addParada("D", "Las Colinas");
        addParada("E", "Los Cerros");
        addParada("F", "Los Jardines");


        addRuta("A", "B",  20,   2.5,   25,  false);
        addRuta("A", "C",   8,   5.0,   20,  false);
        addRuta("B", "C",  10,   3.5,   50,  false);
        addRuta("B", "D",   5,   4.0,   90,  true);
        addRuta("C", "D",   6,   2.0,   15,  false);
        addRuta("C", "E",  20,   8.0,    8,  false);
        addRuta("D", "E",  15,   6.0,   80,  false);
        addRuta("D", "F",  25,   1.5,   90,  false);
        addRuta("E", "F",   5,   4.5,    8,  true);
    }
}

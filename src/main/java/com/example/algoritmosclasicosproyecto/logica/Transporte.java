package com.example.algoritmosclasicosproyecto.logica;

import com.example.algoritmosclasicosproyecto.bd.Conexion;

import java.util.HashMap;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transporte {
    private Map<String, Parada> paradaMap;
    private Map<String, List<Ruta>> listaRuta;
    private static Transporte instancia;

    private Transporte() {
        this.paradaMap = new HashMap<>();
        this.listaRuta = new HashMap<>();
    }

    public static Transporte getInstancia() {
        if (instancia == null) {
            instancia = new Transporte();

        }
        return instancia;
    }
    public List<Ruta> getRutas() {
        List<Ruta> todas = new ArrayList<>();
        for (List<Ruta> rutas : listaRuta.values()) {
            todas.addAll(rutas);
        }
        return todas;
    }

    public List<Parada> getParadas() {
        return new ArrayList<>(paradaMap.values());
    }


    public void addParada(String id, String nombre) {
        if (!paradaMap.containsKey(id)) {
            double randomX = 50 + (Math.random() * 700);
            double randomY = 50 + (Math.random() * 400);


            String sql = "insert into parada (id, nombre, x, y) values (?,?,?,?)";

            try (Connection conn = Conexion.conectar();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                pstmt.setString(2, nombre);
                pstmt.setDouble(3, randomX);
                pstmt.setDouble(4, randomY);
                pstmt.executeUpdate();

                Parada new_parada = new Parada(id, nombre, randomX, randomY);
                paradaMap.put(id, new_parada);
                listaRuta.put(id, new ArrayList<>());

                System.out.println("Parada agregada con posición aleatoria: " + nombre);

            } catch (SQLException e) {
                System.err.println("Error al insertar parada en la BD: " + e.getMessage());
            }
        } else {
            System.err.println("La parada con ID " + id + " ya existe.");
        }
    }

    public void editParada(String id, String nombre) {
        if (paradaMap.containsKey(id)) {
            String sql = "update parada set nombre = ? where id = ?";

            try (Connection conn = Conexion.conectar();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nombre);
                pstmt.setString(2, id);
                int filas = pstmt.executeUpdate();

                if (filas > 0) {
                    paradaMap.get(id).setNombre(nombre);
                    System.out.println("Parada modificada a: " + nombre);
                }

            } catch (SQLException e) {
                System.err.println("Error al editar parada en BD: " + e.getMessage());
            }
        } else {
            System.err.println("Error: La parada con ID " + id + " no existe.");
        }
    }

    public void deleteParada(String id) {
        if (paradaMap.containsKey(id)) {
            String sql = "delete from parada where id = ?";
            try (Connection conn = Conexion.conectar();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                int filas = pstmt.executeUpdate();

                if (filas > 0) {
                    paradaMap.remove(id);
                    listaRuta.remove(id);
                    for (List<Ruta> rutas : listaRuta.values()) {
                        rutas.removeIf(ruta -> ruta.getDestino().getId().equals(id));
                    }
                    System.out.println("Parada y sus conexiones eliminadas.");
                }

            } catch (SQLException e) {
                System.err.println("Error al eliminar parada en BD: " + e.getMessage());
            }
        } else {
            System.err.println("Error: La parada con ID " + id + " no existe.");
        }
    }

    public void addRuta(String id_Origin, String id_Destination, double tiempo, double distancia, double costo, int trasbordo) {
        if (!paradaMap.containsKey(id_Origin) || !paradaMap.containsKey(id_Destination)) {
            System.err.println("Error: El origen o destino no existe.");
            return;
        }

        if (tiempo < 0 || distancia < 0 || costo < 0) {
            System.err.println("Error: Tiempo, distancia y costo no pueden ser negativos.");
            return;
        }

        for (Ruta r : listaRuta.get(id_Origin)) {
            if (r.getDestino().getId().equals(id_Destination)) {
                System.err.println("Error: Ya existe una ruta de " + id_Origin + " a " + id_Destination);
                return;
            }
        }

        String sql = "insert into ruta (id_origen, id_destino, tiempo_minuto, distancia_km, costo, trasbordo) values (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id_Origin);
            pstmt.setString(2, id_Destination);
            pstmt.setDouble(3, tiempo);
            pstmt.setDouble(4, distancia);
            pstmt.setDouble(5, costo);
            pstmt.setInt(6, trasbordo);

            pstmt.executeUpdate();

            Parada origen = paradaMap.get(id_Origin);
            Parada destino = paradaMap.get(id_Destination);
            listaRuta.get(id_Origin).add(new Ruta(origen, destino, tiempo, distancia, costo, trasbordo));

            System.out.println("Ruta agregada de " + id_Origin + " a " + id_Destination);

        } catch (SQLException e) {
            System.err.println("Error al insertar ruta en la BD: " + e.getMessage());
        }
    }
    public void deleteRuta(String id_origin, String id_destination) {
        if (listaRuta.containsKey(id_origin)) {
            String sql = "delete from ruta where id_origen = ? and id_destino = ?";

            try (Connection conn = Conexion.conectar();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, id_origin);
                pstmt.setString(2, id_destination);

                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    List<Ruta> rutas = listaRuta.get(id_origin);
                    rutas.removeIf(ruta -> ruta.getDestino().getId().equals(id_destination));
                    System.out.println("Ruta eliminada exitosamente.");
                } else {
                    System.err.println("La ruta no existe en la base de datos.");
                }

            } catch (SQLException e) {
                System.err.println("Error al eliminar ruta en BD: " + e.getMessage());
            }
        }
    }

    public void editRuta(String id_origin, String id_destination, double tiempo, double distancia, double costo, int trasbordo) {
        if (listaRuta.containsKey(id_origin)) {
            String sql = "update ruta set tiempo_minuto = ?, distancia_km = ?, costo = ?, trasbordo = ? WHERE id_origen = ? AND id_destino = ?";

            try (Connection conn = Conexion.conectar();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setDouble(1, tiempo);
                pstmt.setDouble(2, distancia);
                pstmt.setDouble(3, costo);
                pstmt.setInt(4, trasbordo);
                pstmt.setString(5, id_origin);
                pstmt.setString(6, id_destination);

                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    List<Ruta> rutas = listaRuta.get(id_origin);
                    boolean encontrada = false;
                    int i = 0;
                    while (i < rutas.size() && !encontrada) {
                        Ruta r = rutas.get(i);
                        if (r.getDestino().getId().equals(id_destination)) {
                            r.setTiempo(tiempo);
                            r.setDistancia(distancia);
                            r.setCosto(costo);
                            r.setTrasbordo(trasbordo);
                            encontrada = true;
                        }
                        i++;
                    }
                    System.out.println("Ruta editada exitosamente.");
                } else {
                    System.err.println("La ruta no existe en la base de datos.");
                }

            } catch (SQLException e) {
                System.err.println("Error al editar ruta en BD: " + e.getMessage());
            }
        } else {
            System.err.println("Error: El origen no existe.");
        }
    }



}

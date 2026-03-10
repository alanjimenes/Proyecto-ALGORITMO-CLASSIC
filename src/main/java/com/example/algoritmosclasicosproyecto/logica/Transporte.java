package com.example.algoritmosclasicosproyecto.logica;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transporte {
    private Map<String, Parada> paradaMap;
    private Map<String, List<Ruta>> listaAdyacencia;
    private static Transporte instancia;
    private Transporte() {
        this.paradaMap = new HashMap<>();
        this.listaAdyacencia = new HashMap<>();
    }

    public static Transporte getInstancia() {
        if (instancia == null) {
            instancia = new Transporte();
            instancia.cargarDatosDesdeBD();
        }
        return instancia;
    }
    public List<Parada> getTodasLasParadas() {
        return new ArrayList<>(paradaMap.values());
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
                    listaAdyacencia.remove(id);
                    for (List<Ruta> rutas : listaAdyacencia.values()) {
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

        String sql = "insert into ruta (id_origen, id_destino, tiempo_minuto, distancia_km, costo, trasbordo) values (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id_Origin);
            pstmt.setString(2, id_Destination);
            pstmt.setDouble(3, tiempo);
            pstmt.setDouble(4, distancia);
            pstmt.setDouble(5, costo);
            pstmt.setBoolean(6, trasbordo);

            pstmt.executeUpdate();

            Parada destino = paradaMap.get(id_Destination);
            listaAdyacencia.get(id_Origin).add(new Ruta(destino, tiempo, distancia, costo, trasbordo));
            System.out.println("Ruta agregada de " + id_Origin + " a " + id_Destination);

        } catch (SQLException e) {
            System.err.println("Error al insertar ruta en la BD: " + e.getMessage());
        }
    }

    public void deleteRuta(String id_origin, String id_destination) {
        if (listaAdyacencia.containsKey(id_origin)) {
            String sql = "delete from ruta where id_origen = ? and id_destino = ?";

            try (Connection conn = Conexion.conectar();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, id_origin);
                pstmt.setString(2, id_destination);

                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    List<Ruta> rutas = listaAdyacencia.get(id_origin);
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

    public void editRuta(String id_origin, String id_destination, double tiempo, double distancia, double costo, boolean trasbordo) {
        if (listaAdyacencia.containsKey(id_origin)) {
            String sql = "update ruta set tiempo_minuto = ?, distancia_km = ?, costo = ?, trasbordo = ? WHERE id_origen = ? AND id_destino = ?";

            try (Connection conn = Conexion.conectar();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setDouble(1, tiempo);
                pstmt.setDouble(2, distancia);
                pstmt.setDouble(3, costo);
                pstmt.setBoolean(4, trasbordo);
                pstmt.setString(5, id_origin);
                pstmt.setString(6, id_destination);

                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
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

    private static class NodoDistancia implements Comparable<NodoDistancia> {
        String idParada;
        double distancia;

        public NodoDistancia(String idParada, double distancia) {
            this.idParada = idParada;
            this.distancia = distancia;
        }

        @Override
        public int compareTo(NodoDistancia otro) {
            return Double.compare(this.distancia, otro.distancia);
        }
    }

    private double obtenerPesoRuta(Ruta ruta, String criterio) {
        switch (criterio.toLowerCase()) {
            case "tiempo": return ruta.getTiempoMinuto();
            case "distancia": return ruta.getDistanciaKm();
            case "costo": return ruta.getCosto();
            default: return ruta.getTiempoMinuto();
        }
    }

    public List<Parada> dijkstra(String id_Origin, String id_Destination, String criterio) {
        if (!paradaMap.containsKey(id_Origin) || !paradaMap.containsKey(id_Destination)) {
            System.err.println("Error: El origen o destino no existe.");
            return null;
        }

        Map<String, Double> distancias = new HashMap<>();
        Map<String, String> anteriores = new HashMap<>();

        PriorityQueue<NodoDistancia> pq = new PriorityQueue<>();

        for (String id : paradaMap.keySet()) {
            distancias.put(id, Double.MAX_VALUE);
        }
        distancias.put(id_Origin, 0.0);
        pq.offer(new NodoDistancia(id_Origin, 0.0));
        boolean destino = false;

        while (!pq.isEmpty() && !destino) {
            NodoDistancia actual = pq.poll();
            String idActual = actual.idParada;

            if (idActual.equals(id_Destination)) {
                destino = true;
            } else {
                if (actual.distancia <= distancias.get(idActual)) {
                    for (Ruta ruta : listaAdyacencia.get(idActual)) {
                        String vecinoId = ruta.getDestino().getId();
                        double peso = obtenerPesoRuta(ruta, criterio);
                        double nuevaDistancia = distancias.get(idActual) + peso;

                        if (nuevaDistancia < distancias.get(vecinoId)) {
                            distancias.put(vecinoId, nuevaDistancia);
                            anteriores.put(vecinoId, idActual);
                            pq.offer(new NodoDistancia(vecinoId, nuevaDistancia));
                        }
                    }
                }
            }
        }

        List<Parada> camino = new ArrayList<>();
        String paso = id_Destination;

        if (distancias.get(id_Destination) == Double.MAX_VALUE) {
            System.err.println("No existe camino posible entre " + id_Origin + " y " + id_Destination);
            return null;
        }

        while (paso != null) {
            camino.add(0, paradaMap.get(paso));
            paso = anteriores.get(paso);
        }

        return camino;
    }


    public void cargarDatosDesdeBD() {
        System.out.println("Iniciando carga del grafo desde Supabase...");

        paradaMap.clear();
        listaAdyacencia.clear();

        String sqlParadas = "select id, nombre from parada";
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sqlParadas);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                paradaMap.put(id, new Parada(id, nombre));
                listaAdyacencia.put(id, new ArrayList<>());
            }
            System.out.println("Vértices cargados: " + paradaMap.size());

        } catch (SQLException e) {
            System.err.println("Error al cargar paradas: " + e.getMessage());
            return;
        }
        String sqlRutas = "select id_origen, id_destino, tiempo_minuto, distancia_km, costo, trasbordo from ruta";
        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sqlRutas);
             ResultSet rs = pstmt.executeQuery()) {

            int contadorRutas = 0;
            while (rs.next()) {
                String origen = rs.getString("id_origen");
                String destino = rs.getString("id_destino");

                if (paradaMap.containsKey(origen) && paradaMap.containsKey(destino)) {
                    double tiempo = rs.getDouble("tiempo_minuto");
                    double distancia = rs.getDouble("distancia_km");
                    double costo = rs.getDouble("costo");
                    boolean trasbordo = rs.getBoolean("trasbordo");

                    Parada paradaDestino = paradaMap.get(destino);
                    listaAdyacencia.get(origen).add(new Ruta(paradaDestino, tiempo, distancia, costo, trasbordo));
                    contadorRutas++;
                }
            }
            System.out.println("Aristas cargadas: " + contadorRutas);

        } catch (SQLException e) {
            System.err.println("Error al cargar rutas: " + e.getMessage());
        }
    }

}

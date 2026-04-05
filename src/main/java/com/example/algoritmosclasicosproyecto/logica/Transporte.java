package com.example.algoritmosclasicosproyecto.logica;

import com.example.algoritmosclasicosproyecto.bd.Conexion;
import com.example.algoritmosclasicosproyecto.mappers.deleteRutaMapper;
import com.example.algoritmosclasicosproyecto.mappers.insertRutaMapper;
import com.example.algoritmosclasicosproyecto.mappers.updateRutaMapper;

import java.sql.*;
import java.util.HashMap;
import java.util.*;

public class Transporte {
    private Map<Integer, Parada> paradaMap;
    private Map<Integer, List<Ruta>> listaRuta;
    private static Transporte instancia;

    public Transporte() {
        this.paradaMap = new HashMap<>();
        this.listaRuta = new HashMap<>();
    }

    public static Transporte getInstancia() {
        if (instancia == null) {
            instancia = new Transporte();

        }
        return instancia;
    }

    public Map<Integer, Parada> getParadaMap() {
        return paradaMap;
    }

    public Map<Integer, List<Ruta>> getListaRuta() {
        return listaRuta;
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


    public void addParada(String nombre) {
        double randomX = 50 + (Math.random() * 700);
        double randomY = 50 + (Math.random() * 400);

        final String SQL = "insert into parada (nombre, x, y) values (?, ?, ?)";
        Connection connection = Conexion.conectar();


        try (PreparedStatement pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nombre);
            pstmt.setDouble(2, randomX);
            pstmt.setDouble(3, randomY);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);


                        Parada nuevaParada = new Parada(generatedId, nombre, randomX, randomY);
                        paradaMap.put(generatedId, nuevaParada);
                        listaRuta.put(generatedId, new ArrayList<>());

                        System.out.println("Parada registrada en BD y memoria con ID: " + generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar parada: " + e.getMessage());
        }
    }

    public void editParada(int id, String nombre) {
        if (!paradaMap.containsKey(id)) {
            System.err.println("La parada con ID " + id + " no existe");
            return;
        }

        String sql = "UPDATE parada SET nombre = ? WHERE id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setInt(2, id);

            int filas = pstmt.executeUpdate();

            if (filas > 0) {

                paradaMap.get(id).setNombre(nombre);
                //   System.out.println("Parada " + id + " modificada a: " + nombre);
            }

        } catch (SQLException e) {
            System.err.println("Error al editar parada en BD: " + e.getMessage());
        }
    }

    public void deleteParada(int id) {
        if (!paradaMap.containsKey(id)) {
            System.err.println("Error: La parada con ID " + id + " no existe.");
            return;
        }

        String sqlDeleteRutas = "delete from ruta where id_origen = ? or id_destino = ?";
        String sqlDeleteParada = "delete from parada where id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmtRutas = conn.prepareStatement(sqlDeleteRutas);
             PreparedStatement pstmtParada = conn.prepareStatement(sqlDeleteParada)) {

            pstmtRutas.setInt(1, id);
            pstmtRutas.setInt(2, id);
            pstmtRutas.executeUpdate();

            pstmtParada.setInt(1, id);
            int filas = pstmtParada.executeUpdate();

            if (filas > 0) {
                paradaMap.remove(id);
                listaRuta.remove(id);

                for (List<Ruta> rutas : listaRuta.values()) {
                    rutas.removeIf(ruta -> ruta.getDestino().getId() == id);
                }

                System.out.println("Parada " + id + " eliminada en cascada con éxito.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar parada en BD: " + e.getMessage());
        }
    }

    public void addRuta(int id_Origin, int id_Destination, double tiempo, double distancia, double costo, int trasbordo) {

        if (!paradaMap.containsKey(id_Origin) || !paradaMap.containsKey(id_Destination)) {
            System.err.println("Error: El origen o destino no existe.");
            return;
        }


        if (tiempo < 0 || distancia < 0 || costo < 0) {
            System.err.println("Error: Tiempo, distancia y costo no pueden ser negativos.");
            return;
        }
        List<Ruta> rutas_origin = listaRuta.get(id_Origin);

        for (Ruta r : listaRuta.get(id_Origin)) {
            if (r.getDestino().getId() == id_Destination) {
                System.err.println("Ya existe una ruta de " + id_Origin + " a " + id_Destination);
                return;
            }
        }

        Parada origen = paradaMap.get(id_Origin);
        Parada destino = paradaMap.get(id_Destination);
        Ruta nuevaRuta = new Ruta(origen, destino, tiempo, distancia, costo, trasbordo);


        StatementService<Ruta> service = (StatementService<Ruta>) StatementService.getInstance();

        try {
            service.executeUpdate(nuevaRuta, new insertRutaMapper() {
            });
            listaRuta.get(id_Origin).add(nuevaRuta);
            System.out.println("Ruta agregada exitosamente en BD y Grafo.");

        } catch (Exception e) {
            System.err.println("Error al procesar ruta: " + e.getMessage());
        }
    }
    public Ruta getRuta(int id_Origin, int id_Destination) {
        if (!listaRuta.containsKey(id_Origin)) return null;
        for (Ruta r : listaRuta.get(id_Origin)) {
            if (r.getDestino().getId() == id_Destination) {
                return r;
            }
        }
        return null;
    }

    public void editRuta(int id_origin, int id_destination, double tiempo, double distancia, double costo, int trasbordo) {

        if (!paradaMap.containsKey(id_origin) || !paradaMap.containsKey(id_destination)) {
            System.err.println("Origen o destino no existen.");
            return;
        }

        if (tiempo < 0 || distancia < 0 || costo < 0 || trasbordo < 0) {
            System.err.println("Error: Los valores no pueden ser negativos.");
            return;
        }
        Parada origen = paradaMap.get(id_origin);
        Parada destino = paradaMap.get(id_destination);
        Ruta rutaEditada = new Ruta(origen, destino, tiempo, distancia, costo, trasbordo);
        StatementService<Ruta> service = (StatementService<Ruta>) StatementService.getInstance();
        try {
            service.executeUpdate(rutaEditada, new updateRutaMapper() {
            });
            List<Ruta> rutas = listaRuta.get(id_origin);
            boolean encontrada = false;
            int i = 0;
            while (i < rutas.size() && !encontrada) {
                Ruta r = rutas.get(i);
                if (r.getDestino().getId() == id_destination) {
                    r.setTiempo(tiempo);
                    r.setDistancia(distancia);
                    r.setCosto(costo);
                    r.setTrasbordo(trasbordo);
                    encontrada = true;
                }
                i++;
            }

            //avisos por dentro para confirmar
            System.out.println("Ruta editada exitosamente.");

        } catch (Exception e) {
            System.err.println("Error al editar " + e.getMessage());
        }
    }

    public void deleteRuta(int id_origin, int id_destination) {
        if (!listaRuta.containsKey(id_origin)) {
            System.err.println("Error: No hay rutas registradas para el origen " + id_origin);
            return;
        }

        Parada origen = paradaMap.get(id_origin);
        Parada destino = paradaMap.get(id_destination);

        if (origen == null || destino == null) {
            System.err.println("Error: Una de las paradas no existe en el sistema.");
            return;
        }

        Ruta ruta_deleted = new Ruta(origen, destino, 0, 0, 0, 0);

        StatementService<Ruta> service = (StatementService<Ruta>) StatementService.getInstance();

        try {
            service.executeUpdate(ruta_deleted, new deleteRutaMapper() {
            });
            listaRuta.get(id_origin).removeIf(r -> r.getDestino().getId() == id_destination);
            System.out.println("Ruta eliminada: " + origen.getNombre() + " => " + destino.getNombre());

        } catch (Exception e) {
            System.err.println("error " + e.getMessage());
        }
    }


    public double calcularTotalRuta(List<Parada> camino, String criterio) {
        if (camino == null || camino.size() < 2) return 0;

        double total = 0;
        for (int i = 0; i < camino.size() - 1; i++) {
            int origenId = camino.get(i).getId();
            int destinoId = camino.get(i + 1).getId();

            List<Ruta> rutasDisponibles = listaRuta.get(origenId);
            if (rutasDisponibles != null) {
                for (Ruta ruta : rutasDisponibles) {
                    if (ruta.getDestino().getId() == destinoId) {
                        total += ruta.getPeso(criterio);
                        break;
                    }
                }
            }
        }
        return total;
    }



    /*
      Usamos el Singleton para que todas las pantallas compartan exactamente los mismos datos en memoria
      Primero limpiamos las listas primero para no duplicar datos si esta función se ejecuta más de una vez.
      Luego cargamos las paradas fila por fila y le preparamos a cada una una lista vacía para guardar sus futuras rutas.
      Y finalmente cargamos las rutas, pero con una regla estricta: antes de agregar una ruta,
      verificamos que la parada de origen y la de destino realmente existan en la memoria.


     */
    public void load_data() {
        paradaMap.clear();
        listaRuta.clear();

        String sqlParadas = "select * from parada";
        String sqlRutas = "select * from ruta";

        try (Connection conn = Conexion.conectar();
             Statement stmt = conn.createStatement()) {
            ResultSet rsParadas = stmt.executeQuery(sqlParadas);
            while (rsParadas.next()) {
                int id = rsParadas.getInt("id");
                Parada p = new Parada(id, rsParadas.getString("nombre"),
                        rsParadas.getDouble("x"), rsParadas.getDouble("y"));
                paradaMap.put(id, p);
                listaRuta.put(id, new ArrayList<>());
            }
            rsParadas.close();

            ResultSet rsRutas = stmt.executeQuery(sqlRutas);
            while (rsRutas.next()) {
                int idOrigen = rsRutas.getInt("id_origen");
                int idDestino = rsRutas.getInt("id_destino");

                if (paradaMap.containsKey(idOrigen) && paradaMap.containsKey(idDestino)) {
                    Ruta r = new Ruta(
                            paradaMap.get(idOrigen),
                            paradaMap.get(idDestino),
                            rsRutas.getDouble("tiempo_minuto"),
                            rsRutas.getDouble("distancia_km"),
                            rsRutas.getDouble("costo"),
                            rsRutas.getInt("trasbordo")
                    );
                    listaRuta.get(idOrigen).add(r);
                }
            }


        } catch (SQLException e) {
            System.err.println("Error al cargar la BD: " + e.getMessage());
        }
    }
}

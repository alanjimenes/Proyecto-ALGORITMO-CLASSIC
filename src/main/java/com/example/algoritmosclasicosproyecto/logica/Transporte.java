package com.example.algoritmosclasicosproyecto.logica;
import com.example.algoritmosclasicosproyecto.bd.Conexion;
import com.example.algoritmosclasicosproyecto.mappers.deleteRutaMapper;
import com.example.algoritmosclasicosproyecto.bd.StatementService;
import com.example.algoritmosclasicosproyecto.mappers.insertRutaMapper;
import com.example.algoritmosclasicosproyecto.mappers.updateRutaMapper;

import java.sql.*;
import java.util.HashMap;
import java.util.*;
//en lista de rutas el integer no representa como tal la ruta si no el punto de partida en donde esa ruta empezara o sea
//el id de origen
public class Transporte {
    private Map<Integer, Parada> paradaMap;
    private Map<Integer, List<Ruta>> listaRuta;
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

    public Map<Integer, Parada> getParadaMap() {
        return paradaMap;
    }

    public Map<Integer, List<Ruta>> getListaRuta() {
        return listaRuta;
    }


    //Toma el origen y va almacenando todas las rutas conectadas a ese origen y va cargando la lista
    //de todas para su posterior uso en memoria
    public List<Ruta> getRutas() {
        List<Ruta> todas = new ArrayList<>();
        for (List<Ruta> rutas : listaRuta.values()) {
            todas.addAll(rutas);
        }
        return todas;
    }

    //Toma todas las paradas y las devuelve para su uso proximo,
    //se usa en el controlador de ruta para cargar los comboboxes, en parada controller para alimentar la lista
    public List<Parada> getParadas() {
        return new ArrayList<>(paradaMap.values());
    }

    public void addParada(String nombre) {
        double randomX = 50 + (Math.random() * 700);
        double randomY = 50 + (Math.random() * 400);
        //al añadir parada se multiplica por 700 en X porque este es el ancho de las coordenadas y
        // por 400 en Y porque es la altura de esta
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

                      // System.out.println("Parada registrada en BD y memoria con ID: " + generatedId);
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

        String sql = "update parada set nombre = ? where id = ?";

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

        String sqlDlRutas = "delete from ruta where id_origen = ? or id_destino = ?";
        String sqlDlParada = "delete from parada where id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement pstmtRutas = conn.prepareStatement(sqlDlRutas);
             PreparedStatement pstmtParada = conn.prepareStatement(sqlDlParada)) {

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
            //Aquí simplemente llenamos en memoria la lista en base al id de origen por ejemplo si
            // L va a K y L va a Z se llenan todas las rutas de mi origen que es L en memoria

        } catch (Exception e) {
            System.err.println("Error al procesar ruta: " + e.getMessage());
        }
    }


//Busqueda de conexion
    public Ruta getConexion(int id_Origin, int id_Destination) {
        if (!listaRuta.containsKey(id_Origin)) return null;
        for (Ruta r : listaRuta.get(id_Origin)) {
            if (r.getDestino().getId() == id_Destination) {
                return r;
            }
        }
        return null;
    }

    public void editRuta(int id_origin, int id_destination, double tiempo, double distancia, double costo, int trasbordo) {
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


          //  System.out.println("Ruta editada exitosamente.");

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

        } catch (Exception e) {
            System.err.println("error " + e.getMessage());
        }
    }



    public void load_data() {
        paradaMap.clear();
        listaRuta.clear();

        String sqlParadas = "select * from parada";
        String sqlRutas = "select * from ruta";

        try (Connection conn = Conexion.conectar()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement()) {
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
                rsRutas.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar la BD: " + e.getMessage());
        }
    }
}

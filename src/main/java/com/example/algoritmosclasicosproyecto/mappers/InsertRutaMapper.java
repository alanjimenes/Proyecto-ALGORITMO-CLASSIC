package com.example.algoritmosclasicosproyecto.mappers;

import com.example.algoritmosclasicosproyecto.logica.Ruta;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertRutaMapper {
    public static void execute(Ruta ruta, Connection conn) throws SQLException {
        String sql = "INSERT INTO ruta (id_origen, id_destino, tiempo_minuto, distancia_km, costo, trasbordo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ruta.getOrigen().getId());
            ps.setInt(2, ruta.getDestino().getId());
            ps.setDouble(3, ruta.getTiempo());
            ps.setDouble(4, ruta.getDistancia());
            ps.setDouble(5, ruta.getCosto());
            ps.setInt(6, ruta.getTrasbordo());
            ps.executeUpdate();
        }
    }
}
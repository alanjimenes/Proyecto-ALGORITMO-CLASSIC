package com.example.algoritmosclasicosproyecto.mappers;

import com.example.algoritmosclasicosproyecto.logica.Ruta;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;

public class UpdateRutaMapper {
    public static void execute(Ruta ruta, Connection conn) throws SQLException {
        String sql = "update ruta set tiempo_minuto = ?, distancia_km = ?, costo = ?, trasbordo = ? where id_origen = ? AND id_destino = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, ruta.getTiempo());
            ps.setDouble(2, ruta.getDistancia());
            ps.setDouble(3, ruta.getCosto());
            ps.setInt(4, ruta.getTrasbordo());
            ps.setInt(5, ruta.getOrigen().getId());
            ps.setInt(6, ruta.getDestino().getId());
            ps.executeUpdate();
        }
    }
}
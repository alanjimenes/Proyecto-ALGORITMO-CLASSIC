package com.example.algoritmosclasicosproyecto.mappers;

import com.example.algoritmosclasicosproyecto.logica.Ruta;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;

public class DeleteRutaMapper {
    public static void execute(Ruta ruta, Connection conn) throws SQLException {
        String sql = "delete from ruta where id_origen = ? and id_destino = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ruta.getOrigen().getId());
            ps.setInt(2, ruta.getDestino().getId());
            ps.executeUpdate();
        }
    }
}
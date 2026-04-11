package com.example.algoritmosclasicosproyecto.mappers;

import com.example.algoritmosclasicosproyecto.logica.Ruta;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class insertRutaMapper implements PrepareStatementMapper<Ruta> {

    @Override
    public int execute(Ruta ruta, PreparedStatement ps) throws SQLException {
        ps.setInt(1, ruta.getOrigen().getId());
        ps.setInt(2, ruta.getDestino().getId());
        ps.setDouble(3, ruta.getTiempo());
        ps.setDouble(4, ruta.getDistancia());
        ps.setDouble(5, ruta.getCosto());
        ps.setInt(6, ruta.getTrasbordo());
        return ps.executeUpdate();
    }

    @Override
    public String query() {
        return "insert INTO ruta (id_origen, id_destino, tiempo_minuto, distancia_km, costo, trasbordo) values (?, ?, ?, ?, ?, ?)";
    }
}
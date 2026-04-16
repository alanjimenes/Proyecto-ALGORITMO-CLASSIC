package com.example.algoritmosclasicosproyecto.mappers;

import com.example.algoritmosclasicosproyecto.logica.Ruta;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class deleteRutaMapper implements PrepareStatementMapper<Ruta> {

    @Override
    public void execute(Ruta ruta, PreparedStatement ps) throws SQLException {

        ps.setInt(1, ruta.getOrigen().getId());
        ps.setInt(2, ruta.getDestino().getId());
        ps.executeUpdate();
    }

    @Override
    public String query() {
        return "delete from ruta where id_origen = ? and id_destino = ?";
    }
}
package com.example.algoritmosclasicosproyecto.mappers;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.mappers.PrepareStatementMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface insertRutaMapper extends PrepareStatementMapper<Ruta> {

    @Override
    default int execute(Ruta ruta, PreparedStatement ps) throws SQLException {
        ps.setInt(1, ruta.getOrigen().getId());
        ps.setInt(2, ruta.getDestino().getId());
        ps.setDouble(3, ruta.getTiempo());
        ps.setDouble(4, ruta.getDistancia());
        ps.setDouble(5, ruta.getCosto());
        ps.setInt(6, ruta.getTrasbordo());
        return ps.executeUpdate();
    }

    @Override
    default String query() {
        return "INSERT INTO ruta (id_origen, id_destino, tiempo_minuto, distancia_km, costo, trasbordo) VALUES (?, ?, ?, ?, ?, ?)";
    }
}


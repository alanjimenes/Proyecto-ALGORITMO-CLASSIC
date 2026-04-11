package com.example.algoritmosclasicosproyecto.mappers;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.mappers.PrepareStatementMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public interface updateRutaMapper extends PrepareStatementMapper<Ruta> {
    @Override
    default int execute(Ruta ruta, PreparedStatement ps) throws SQLException {

        ps.setDouble(1, ruta.getTiempo());
        ps.setDouble(2, ruta.getDistancia());
        ps.setDouble(3, ruta.getCosto());
        ps.setInt(4, ruta.getTrasbordo());
        ps.setInt(5, ruta.getOrigen().getId());
        ps.setInt(6, ruta.getDestino().getId());

        return ps.executeUpdate();
    }

    @Override
    default String query() {
        return "update ruta set tiempo_minuto = ?, distancia_km = ?, costo = ?, trasbordo = ? where id_origen = ? AND id_destino = ?";
    }
}

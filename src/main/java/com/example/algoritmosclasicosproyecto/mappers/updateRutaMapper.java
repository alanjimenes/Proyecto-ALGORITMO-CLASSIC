package com.example.algoritmosclasicosproyecto.mappers;

import com.example.algoritmosclasicosproyecto.logica.Ruta;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementación concreta para la actualización de rutas.
 * Cumple con el principio de abstracción al separar la definición (interfaz)
 * de la implementación técnica (esta clase).
 */
public class updateRutaMapper implements PrepareStatementMapper<Ruta> {

    @Override
    public void execute(Ruta ruta, PreparedStatement ps) throws SQLException {
        ps.setDouble(1, ruta.getTiempo());
        ps.setDouble(2, ruta.getDistancia());
        ps.setDouble(3, ruta.getCosto());
        ps.setInt(4, ruta.getTrasbordo());
        ps.setInt(5, ruta.getOrigen().getId());
        ps.setInt(6, ruta.getDestino().getId());

        ps.executeUpdate();
    }

    @Override
    public String query() {
        return "update ruta SET tiempo_minuto = ?, distancia_km = ?, costo = ?, trasbordo = ? where id_origen = ? and id_destino = ?";
    }
}
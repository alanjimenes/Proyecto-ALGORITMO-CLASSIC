package com.example.algoritmosclasicosproyecto.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:postgresql://db.oxysriuzwkgsocmdvmst.supabase.co:5432/postgres?user=postgres&password=Daurito2015777";
    public static Connection conectar() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            return conn;
        } catch (SQLException e) {
            System.err.println("Error de conexio  ncCn a la base de datos: " + e.getMessage());
            return null;
        }
    }


}
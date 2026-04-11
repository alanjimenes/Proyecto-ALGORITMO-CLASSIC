package com.example.algoritmosclasicosproyecto.bd;
import com.example.algoritmosclasicosproyecto.mappers.PrepareStatementMapper;

import java.sql.*;

public final class StatementService<T> {

    private static StatementService<?> INSTANCE;

    private StatementService() {
    }

    public static StatementService<?> getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StatementService<>();
        }
        return INSTANCE;
    }

    public void executeUpdate(T object, PrepareStatementMapper<T> mapper) {
        Connection connection = Conexion.conectar();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(mapper.query());
            mapper.execute(object, preparedStatement);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}

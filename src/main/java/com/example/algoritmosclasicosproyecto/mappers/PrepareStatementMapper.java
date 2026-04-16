package com.example.algoritmosclasicosproyecto.mappers;


import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PrepareStatementMapper<T> {

    void execute(T object, PreparedStatement ps) throws SQLException;
    String query();

}


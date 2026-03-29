module com.example.algoritmosclasicosproyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.postgresql.jdbc;

    opens com.example.algoritmosclasicosproyecto to javafx.fxml;
    exports com.example.algoritmosclasicosproyecto;
    exports com.example.algoritmosclasicosproyecto.controladores;
    opens com.example.algoritmosclasicosproyecto.controladores to javafx.fxml;
    exports com.example.algoritmosclasicosproyecto.logica;
    opens com.example.algoritmosclasicosproyecto.logica to javafx.fxml;
}
module com.example.algoritmosclasicosproyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.algoritmosclasicosproyecto to javafx.fxml;
    exports com.example.algoritmosclasicosproyecto;
}
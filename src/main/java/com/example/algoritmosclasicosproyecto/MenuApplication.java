package com.example.algoritmosclasicosproyecto;

import com.example.algoritmosclasicosproyecto.logica.Transporte;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class MenuApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MenuApplication.class.getResource("menu-view.fxml"));
        Transporte.getInstancia().load_data();

        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);

        stage.setTitle("Menu Principal");
        stage.setScene(scene);
        stage.show();
    }
}

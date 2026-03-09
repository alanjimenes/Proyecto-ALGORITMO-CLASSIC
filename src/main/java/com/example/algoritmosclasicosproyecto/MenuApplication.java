package com.example.algoritmosclasicosproyecto;

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
        Dimension dim = new Dimension();
        dim.setSize(Toolkit.getDefaultToolkit().getScreenSize().width-20,Toolkit.getDefaultToolkit().getScreenSize().height-90);
        Scene scene = new Scene(fxmlLoader.load(), dim.getWidth(), dim.getHeight());
        stage.setTitle("Menu Principal");
        stage.setScene(scene);
        stage.show();
    }
}

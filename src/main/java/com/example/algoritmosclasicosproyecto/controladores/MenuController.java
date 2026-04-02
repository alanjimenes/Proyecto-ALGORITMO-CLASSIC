package com.example.algoritmosclasicosproyecto.controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.util.Objects;

public class MenuController {

    @FXML
    public Button btnInicio, btnMapa, btnRutas, btnParadas;

    @FXML
    public BorderPane pnlContenedor;

    @FXML
    public void btnInicioClick() {
        estilizarBotones(btnInicio);
        pnlContenedor.getChildren().clear();
    }

    @FXML
    public void btnMapaClick() {
        estilizarBotones(btnMapa);
        cambiarPanel("mapa");
    }

    @FXML
    public void btnRutasClick() {
        estilizarBotones(btnRutas);
        cambiarPanel("rutas");
    }

    @FXML
    public void btnParadasClick() {
        estilizarBotones(btnParadas);
        cambiarPanel("paradas");
    }

    public void estilizarBotones(Button btn) {
        String estiloBase = "-fx-background-color: transparent; -fx-text-fill: white;";
        String estiloClick = "-fx-background-color: #3F326D; -fx-text-fill: white;";

        btnInicio.setStyle(btn == btnInicio ? estiloClick : estiloBase);
        btnMapa.setStyle(btn == btnMapa ? estiloClick : estiloBase);
        btnRutas.setStyle(btn == btnRutas ? estiloClick : estiloBase);
        btnParadas.setStyle(btn == btnParadas ? estiloClick : estiloBase);
    }

    public void cambiarPanel(String nombre) {
        try {
            AnchorPane contenido = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/algoritmosclasicosproyecto/" + nombre + "-view.fxml")));
            pnlContenedor.getChildren().clear();
            pnlContenedor.setCenter(contenido);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
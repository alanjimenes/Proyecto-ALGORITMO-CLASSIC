package com.example.algoritmosclasicosproyecto.controladores;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class MenuController {

    @FXML public Button btnMapa, btnRutas, btnParadas;
    @FXML public BorderPane pnlContenedor;

    @FXML public Label lblBienvenida;

    @FXML
    public void initialize() {
        iniciarReloj();
        btnMapaClick();
    }


    private void iniciarReloj() {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy | HH:mm:ss", new Locale("es", "ES"));

        Timeline reloj = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime ahora = LocalDateTime.now();
            String fechaHora = formateador.format(ahora);
            fechaHora = fechaHora.substring(0, 1).toUpperCase() + fechaHora.substring(1);
            lblBienvenida.setText("Bienvenido | " + fechaHora);
        }), new KeyFrame(Duration.seconds(1)));

        reloj.setCycleCount(Animation.INDEFINITE);
        reloj.play();
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
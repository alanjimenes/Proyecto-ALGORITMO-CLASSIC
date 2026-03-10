package com.example.algoritmosclasicosproyecto.controladores;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.List;

public class MapaController {

    @FXML
    private Pane paneMapa;

    @FXML
    public void initialize() {
        dibujarGrafo();
    }

    public void dibujarGrafo() {
        if (paneMapa == null) return;


        paneMapa.getChildren().clear();

        List<Parada> paradas = Transporte.getInstancia().getParadas();
        List<Ruta> rutas = Transporte.getInstancia().getRutas();

        for (int i = 0; i < rutas.size(); i++) {
            Ruta ruta = rutas.get(i);
            Parada origen = ruta.getOrigen();
            Parada destino = ruta.getDestino();

            if (origen != null && destino != null) {
                Line linea = new Line(origen.getX(), origen.getY(), destino.getX(), destino.getY());
                linea.setStroke(Color.DARKGRAY);
                linea.setStrokeWidth(2.5);
                paneMapa.getChildren().add(linea);
            }
        }

        for (int i = 0; i < paradas.size(); i++) {
            final Parada parada = paradas.get(i);

            Circle circulo = new Circle(20, Color.web("#088395"));
            circulo.setStroke(Color.WHITE);
            circulo.setStrokeWidth(2.0);

            Text texto = new Text(parada.getNombre());
            texto.setFill(Color.BLACK);
            texto.setStyle("-fx-font-weight: bold;");

            final StackPane nodoVisual = new StackPane();
            nodoVisual.getChildren().addAll(circulo, texto);

            nodoVisual.setLayoutX(parada.getX() - 20);
            nodoVisual.setLayoutY(parada.getY() - 20);

            nodoVisual.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    double nuevoX = event.getSceneX();
                    double nuevoY = event.getSceneY();

                    parada.setX(nuevoX);
                    parada.setY(nuevoY);


                    dibujarGrafo();
                }
            });

            paneMapa.getChildren().add(nodoVisual);
        }
    }
}
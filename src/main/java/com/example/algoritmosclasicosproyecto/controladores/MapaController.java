package com.example.algoritmosclasicosproyecto.controladores;

import com.example.algoritmosclasicosproyecto.algoritmos.Dijkstra;
import com.example.algoritmosclasicosproyecto.algoritmos.RutasAlternativas;
import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapaController {

    @FXML private Pane paneMapa;
    @FXML private ComboBox<Parada> cmbOrigen;
    @FXML private ComboBox<Parada> cmbDestino;
    @FXML private ComboBox<String> cmbCriterio;


    @FXML private VBox panelInfo;
    @FXML private Label lblTrayecto;
    @FXML private Label lblCriterioInfo;
    @FXML private Label lblTotalInfo;

    private final double RADIO_NODO = 20.0;
    private final double RADIO_VISUAL = 22.0;

    private List<Ruta> colorCamino = new ArrayList<>();

    private double mouseX;
    private double mouseY;

    @FXML
    public void initialize() {
        configurarUI();
        dibujarGrafo();
        panelInfo.setVisible(false);
        arrastrar();
    }

    private void configurarUI() {
        cmbCriterio.setItems(FXCollections.observableArrayList("Seleccione","Tiempo", "Distancia", "Costo", "Trasbordo"));
        cmbCriterio.setValue("Seleccione");
        ObservableList<Parada> paradas = FXCollections.observableArrayList(Transporte.getInstancia().getParadas());
        cmbOrigen.setItems(paradas);
        cmbDestino.setItems(paradas);

        StringConverter<Parada> converter = new StringConverter<>() {
            @Override public String toString(Parada p) { return p == null ? "" : p.getNombre(); }
            @Override public Parada fromString(String s) { return null; }
        };
        cmbOrigen.setConverter(converter);
        cmbDestino.setConverter(converter);
    }

    private void arrastrar() {
        panelInfo.setOnMousePressed(event -> {
            mouseX = event.getSceneX() - panelInfo.getTranslateX();
            mouseY = event.getSceneY() - panelInfo.getTranslateY();
        });

        panelInfo.setOnMouseDragged(event -> {
            panelInfo.setTranslateX(event.getSceneX() - mouseX);
            panelInfo.setTranslateY(event.getSceneY() - mouseY);
        });
    }

    @FXML
    void closepanel(ActionEvent event) {
        panelInfo.setVisible(false);
    }

    public void dibujarGrafo() {
        if (paneMapa == null) return;
        paneMapa.getChildren().clear();

        List<Parada> paradas = Transporte.getInstancia().getParadas();
        List<Ruta> rutas = Transporte.getInstancia().getRutas();

        for (Ruta ruta : rutas) {
            Parada origen = ruta.getOrigen();
            Parada destino = ruta.getDestino();

            if (origen != null && destino != null) {
                boolean optimo = colorCamino.contains(ruta);
                Color colorRuta = optimo ? Color.GOLD : Color.web("#8D99EE");
                double grosor = optimo ? 4.0 : 2.5;

                dibujarRuta(origen.getX(), origen.getY(), destino.getX(), destino.getY(), colorRuta, grosor);
            }
        }

        for (Parada parada : paradas) {
            Group nodoVisual = createNodo(parada);
            nodoVisual.setLayoutX(parada.getX());
            nodoVisual.setLayoutY(parada.getY());
            paneMapa.getChildren().add(nodoVisual);
        }
    }

    private Group createNodo(Parada parada) {
        Group contenedor = new Group();

        Circle circulo = new Circle(0, 0, RADIO_NODO, Color.web("#62529C"));
        circulo.setStroke(Color.WHITE);
        circulo.setStrokeWidth(2.0);

        Text txtId = new Text(String.valueOf(parada.getId()));
        txtId.setFill(Color.WHITE);
        txtId.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-font-family: Consolas;");

        double anchoTexto = txtId.getLayoutBounds().getWidth();
        double altoTexto = txtId.getLayoutBounds().getHeight();
        txtId.setX(-anchoTexto / 2);
        txtId.setY(altoTexto / 4);

        Text txtNombre = new Text(parada.getNombre());
        txtNombre.setFill(Color.web("#333333"));
        txtNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        txtNombre.setTextAlignment(TextAlignment.CENTER);

        double anchoNombre = txtNombre.getLayoutBounds().getWidth();
        txtNombre.setX(-anchoNombre / 2);
        txtNombre.setY(RADIO_NODO + 15);

        contenedor.getChildren().addAll(circulo, txtId, txtNombre);

        return contenedor;
    }

    private void dibujarRuta(double startX, double startY, double endX, double endY, Color color, double grosor) {
        double dx = endX - startX;
        double dy = endY - startY;
        double angulo = Math.atan2(dy, dx);

        double inicioX = startX + RADIO_VISUAL * Math.cos(angulo);
        double inicioY = startY + RADIO_VISUAL * Math.sin(angulo);
        double finalX = endX - RADIO_VISUAL * Math.cos(angulo);
        double finalY = endY - RADIO_VISUAL * Math.sin(angulo);

        Line linea = new Line(inicioX, inicioY, finalX, finalY);
        linea.setStroke(color);
        linea.setStrokeWidth(grosor);

        Polygon flecha = new Polygon();
        double tamFlecha = 10.0;
        flecha.getPoints().addAll(
                finalX, finalY,
                finalX - tamFlecha * Math.cos(angulo - Math.PI / 6), finalY - tamFlecha * Math.sin(angulo - Math.PI / 6),
                finalX - tamFlecha * Math.cos(angulo + Math.PI / 6), finalY - tamFlecha * Math.sin(angulo + Math.PI / 6)
        );

        flecha.setFill(color == Color.GOLD ? Color.GOLD : color.darker());

        paneMapa.getChildren().addAll(linea, flecha);
    }



    @FXML
    void best_ruta(ActionEvent event) {
        ejecutarBusqueda(false);
    }

    @FXML
    void ruta_alternativa(ActionEvent event) {
        ejecutarBusqueda(true);
    }


    private void ejecutarBusqueda(boolean esAlternativa) {
        Parada o = cmbOrigen.getValue();
        Parada d = cmbDestino.getValue();
        String criterio = cmbCriterio.getValue();

        panelInfo.setVisible(false);

        if (o == null || d == null) {
            alert("Error de Seleccion", "Debe seleccionar un origen y un destino.", Alert.AlertType.WARNING);
            return;
        }

        if (o.getId() == d.getId()) {
            alert("Error:", "El origen y el destino no pueden ser la misma parada.", Alert.AlertType.WARNING);
            return;
        }

        if (criterio == null || criterio.equals("Seleccione")) {
            alert("Error:", "Debe seleccionar un criterio de prioridad válido (Tiempo, Distancia, etc.).", Alert.AlertType.WARNING);
            return;
        }

        List<Parada> caminoNodos;


        String criterioBusqueda = criterio.toLowerCase();

        if (esAlternativa) {

            Map<String, List<List<Parada>>> mapaResultados = RutasAlternativas.getRutas(Transporte.getInstancia(), o.getId(), d.getId());
            List<List<Parada>> opciones = mapaResultados.get(criterioBusqueda);


            if (opciones == null || opciones.size() < 2) {
                alert("Sin Alternativas", "No se encontró una segunda ruta válida para este trayecto.", Alert.AlertType.INFORMATION);
                return;
            }
            caminoNodos = opciones.get(1);
        } else {

            caminoNodos = Dijkstra.dijkstra(Transporte.getInstancia(), o.getId(), d.getId(), criterioBusqueda);
        }

        colorCamino.clear();

        if (caminoNodos == null || caminoNodos.size() < 2) {
            alert("Ruta no encontrada", "No existe un camino posible conectando estos dos puntos.", Alert.AlertType.ERROR);
            dibujarGrafo();
            return;
        }

        double total = 0;
        Map<Integer, List<Ruta>> listaRuta = Transporte.getInstancia().getListaRuta();

        for (int i = 0; i < caminoNodos.size() - 1; i++) {
            Parada actual = caminoNodos.get(i);
            Parada siguiente = caminoNodos.get(i + 1);

            List<Ruta> rutasDesdeActual = listaRuta.getOrDefault(actual.getId(), new ArrayList<>());
            for (Ruta r : rutasDesdeActual) {
                if (r.getDestino().getId() == siguiente.getId()) {
                    colorCamino.add(r);
                    total += r.getPeso(criterioBusqueda);
                    break;
                }
            }
        }

        dibujarGrafo();


        String etiquetaCriterio = esAlternativa ? criterio + " (Alternativa)" : criterio;
        showInfoRuta(o, d, etiquetaCriterio, total);

        panelInfo.setTranslateX(0);
        panelInfo.setTranslateY(0);
    }



    private void showInfoRuta(Parada origen, Parada destino, String criterio, double total) {
        lblTrayecto.setText(origen.getNombre() + " -> " + destino.getNombre());
        lblCriterioInfo.setText(criterio);


        String critPuro = criterio.replace(" (Alternativa)", "");

        if (critPuro.equalsIgnoreCase("Trasbordo")) {
            lblTotalInfo.setText(String.format("%d unidades", (int)total));
        } else if (critPuro.equalsIgnoreCase("Costo")) {
            lblTotalInfo.setText(String.format("RD$ %.2f", total));
        } else if (critPuro.equalsIgnoreCase("Tiempo")) {
            lblTotalInfo.setText(String.format("%.2f min", total));
        } else {
            lblTotalInfo.setText(String.format("%.2f km", total));
        }

        panelInfo.setVisible(true);
    }

    @FXML
    void limpiarBusqueda(ActionEvent event) {
        colorCamino.clear();
        cmbOrigen.getSelectionModel().clearSelection();
        cmbDestino.getSelectionModel().clearSelection();
        cmbCriterio.setValue("Seleccione");
        panelInfo.setVisible(false);
        dibujarGrafo();
    }

    private void alert(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
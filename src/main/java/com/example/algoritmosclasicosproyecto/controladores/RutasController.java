package com.example.algoritmosclasicosproyecto.controladores;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
public class RutasController {

    @FXML private ComboBox<Parada> cmbOrigen;
    @FXML private ComboBox<Parada> cmbDestino;
    @FXML private TextField txtTiempo;
    @FXML private TextField txtDistancia;
    @FXML private TextField txtCosto;
    @FXML private CheckBox chkTrasbordo;
    @FXML private TableView<Ruta> tablaRutas;
    @FXML private TableColumn<Ruta, String> colOrigen;
    @FXML private TableColumn<Ruta, String> colDestino;
    @FXML private TableColumn<Ruta, Double> colTiempo;
    @FXML private TableColumn<Ruta, Double> colDistancia;
    @FXML private TableColumn<Ruta, Double> colCosto;
    @FXML private TableColumn<Ruta, Boolean> colTrasbordo;

    @FXML
    public void initialize() {
        comboList();

        cargarDatos();
    }

    private void cargarDatos() {

        ObservableList<Parada> paradas = FXCollections.observableArrayList(Transporte.getInstancia().getParadas());
        cmbOrigen.setItems(paradas);
        cmbDestino.setItems(paradas);

    }

    @FXML
    void agregarRuta(ActionEvent event) {

        Parada origen = cmbOrigen.getValue();
        Parada destino = cmbDestino.getValue();

        if (origen == null || destino == null) {
            mostrarAlerta("Error", "Debe seleccionar origen y destino.");
            return;
        }

        if (origen.getId().equals(destino.getId())) {
            mostrarAlerta("Error de Lógica", "El origen y el destino no pueden ser iguales.");
            return;
        }

        try {
            double tiempo = Double.parseDouble(txtTiempo.getText().trim());
            double distancia = Double.parseDouble(txtDistancia.getText().trim());
            double costo = Double.parseDouble(txtCosto.getText().trim());

            if (tiempo <= 0 || distancia <= 0 || costo < 0) {
                mostrarAlerta("Datos Inválidos", "Tiempo y distancia deben ser > 0. El costo no puede ser negativo.");
                return;
            }

            boolean trasbordo = chkTrasbordo.isSelected();
            Transporte.getInstancia().addRuta(origen.getId(), destino.getId(), tiempo, distancia, costo, trasbordo);


            mostrarAlerta("Éxito", "Ruta creada de " + origen.getNombre() + " a " + destino.getNombre());
            limpiarFormulario();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "Ingrese valores numéricos válidos en tiempo, distancia y costo.");
        }
    }

    private void limpiarFormulario() {
        cmbOrigen.getSelectionModel().clearSelection();
        cmbDestino.getSelectionModel().clearSelection();
        txtTiempo.clear();
        txtDistancia.clear();
        txtCosto.clear();
        chkTrasbordo.setSelected(false);
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    private void comboList() {
        StringConverter<Parada> converter = new StringConverter<>() {
            @Override
            public String toString(Parada parada) {
                return parada == null ? "" : parada.getNombre();
            }
            @Override
            public Parada fromString(String string) {
                return null;
            }
        };
        cmbOrigen.setConverter(converter);
        cmbDestino.setConverter(converter);
    }




}
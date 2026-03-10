package com.example.algoritmosclasicosproyecto.controladores;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Transporte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ParadasController {

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private Button btnAgregar, btnEditar, btnEliminar;

    @FXML private TableView<Parada> tablaParadas;
    @FXML private TableColumn<Parada, String> colId;
    @FXML private TableColumn<Parada, String> colNombre;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getId()));
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));

        actualizarTabla();

        tablaParadas.getSelectionModel().selectedItemProperty().addListener((obs, viejaSeleccion, nuevaSeleccion) -> {
            if (nuevaSeleccion != null) {
                txtId.setText(nuevaSeleccion.getId());
                txtNombre.setText(nuevaSeleccion.getNombre());
                txtId.setDisable(true);
                btnAgregar.setDisable(true);
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
            }
        });

        limpiarFormulario();
    }

    @FXML
    void agregarParada(ActionEvent event) {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (id.isEmpty() || nombre.isEmpty())
            return;

        Transporte.getInstancia().addParada(id, nombre);
        actualizarTabla();
        limpiarFormulario();
    }

    @FXML
    void editarParada(ActionEvent event) {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (id.isEmpty() || nombre.isEmpty())
             return;

        Transporte.getInstancia().editParada(id, nombre);
        actualizarTabla();
        limpiarFormulario();
    }

    @FXML
    void eliminarParada(ActionEvent event) {
        String id = txtId.getText().trim();
        if (id.isEmpty()) return;

        Transporte.getInstancia().deleteParada(id);
        actualizarTabla();
        limpiarFormulario();
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtNombre.clear();
        txtId.setDisable(false);
        tablaParadas.getSelectionModel().clearSelection();

        btnAgregar.setDisable(false);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    private void actualizarTabla() {
        ObservableList<Parada> lista = FXCollections.observableArrayList(Transporte.getInstancia().getTodasLasParadas());
        tablaParadas.setItems(lista);
    }
}
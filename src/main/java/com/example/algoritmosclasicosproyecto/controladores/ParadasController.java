package com.example.algoritmosclasicosproyecto.controladores;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Transporte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

        updateTabla();

        tablaParadas.getSelectionModel().selectedItemProperty().addListener((obs, antigua, nueva) -> {
            if (nueva != null) {
                txtId.setText(nueva.getId());
                txtNombre.setText(nueva.getNombre());
                txtId.setDisable(true);
                btnAgregar.setDisable(true);
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
            }
        });

        cleanForm();
    }

    @FXML
    void addParada(ActionEvent event) {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (id.isEmpty() || nombre.isEmpty()) {
            alerta("Error", "ID y Nombre son obligatorios.");
            return;
        }

        Transporte.getInstancia().addParada(id, nombre);

        updateTabla();
        cleanForm();
    }
    @FXML
    void editParada(ActionEvent event) {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (id.isEmpty() || nombre.isEmpty())
             return;

        Transporte.getInstancia().editParada(id, nombre);
        updateTabla();
        cleanForm();
    }

    @FXML
    void deleteParada(ActionEvent event) {
        String id = txtId.getText().trim();
        if (id.isEmpty()) return;

        Transporte.getInstancia().deleteParada(id);
        updateTabla();
        cleanForm();
    }

    @FXML
    void cleanForm(ActionEvent event) {
        cleanForm();
    }

    private void cleanForm() {
        txtId.clear();
        txtNombre.clear();
        txtId.setDisable(false);
        tablaParadas.getSelectionModel().clearSelection();

        btnAgregar.setDisable(false);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    private void updateTabla() {
        ObservableList<Parada> lista = FXCollections.observableArrayList(Transporte.getInstancia().getParadas());
        tablaParadas.refresh();
        tablaParadas.setItems(lista);
    }

    private void alerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

}
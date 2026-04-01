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
    @FXML private TableColumn<Parada, Integer> colId;
    @FXML private TableColumn<Parada, String> colNombre;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        txtId.setDisable(true);

        updateTabla();


        tablaParadas.getSelectionModel().selectedItemProperty().addListener((obs, antigua, nueva) -> {
            if (nueva != null) {
                txtId.setText(String.valueOf(nueva.getId()));
                txtNombre.setText(nueva.getNombre());

                btnAgregar.setDisable(true);
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
            }
        });

        cleanForm();
    }

    @FXML
    void agregarParada(ActionEvent event) {
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            alerta("Error", "El nombre es obligatorio.");
            return;
        }

        Transporte.getInstancia().addParada(nombre);

        updateTabla();
        cleanForm();
    }

    @FXML
    void editarParada(ActionEvent event) {
        String idStr = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (idStr.isEmpty() || nombre.isEmpty()) return;

        int id = Integer.parseInt(idStr); // Convertir a int
        Transporte.getInstancia().editParada(id, nombre);

        updateTabla();
        cleanForm();
    }

    @FXML
    void eliminarParada(ActionEvent event) {
        String idStr = txtId.getText().trim();

        if (idStr.isEmpty()) return;

        int id = Integer.parseInt(idStr); // Convertir a int
        Transporte.getInstancia().deleteParada(id);

        updateTabla();
        cleanForm();
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        cleanForm();
    }

    private void cleanForm() {
        txtId.clear();
        txtNombre.clear();
        tablaParadas.getSelectionModel().clearSelection();

        btnAgregar.setDisable(false);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    private void updateTabla() {
        ObservableList<Parada> lista = FXCollections.observableArrayList(Transporte.getInstancia().getParadas());
        tablaParadas.setItems(lista);
        tablaParadas.refresh();
    }

    private void alerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
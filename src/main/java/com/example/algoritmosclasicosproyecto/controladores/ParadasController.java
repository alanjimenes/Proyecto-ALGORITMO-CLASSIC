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
            } else {
                cleanForm();
            }
        });

        cleanForm();


        inyeccionCss();
        anchoTabla();
    }


    private void inyeccionCss(){
        String css = """
            .table-view {
                -fx-background-color: transparent;
                -fx-border-color: #E0E0E0;
                -fx-border-radius: 5px;
            }
            .table-view .column-header-background,
            .table-view .column-header-background .filler {
                -fx-background-color: #62529C; 
            }
            .table-view .column-header {
                -fx-background-color: transparent;
                -fx-size: 40px;
                -fx-border-width: 0 1px 0 0;
                -fx-border-color: #3F326D;
            }
            .table-view .column-header .label {
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-font-family: "Consolas";
                -fx-alignment: center; 
            }
            .table-view .table-cell {
                -fx-alignment: center;
                -fx-font-size: 13px;
                -fx-padding: 5px;
                -fx-text-fill: #333333;
            }
            .table-view .table-row-cell:selected {
                -fx-background-color: #8D99EE;
            }
            .table-view .table-row-cell:selected .text {
                -fx-fill: white;
            }
            """;
        String base64Css = java.util.Base64.getEncoder().encodeToString(css.getBytes());
        tablaParadas.getStylesheets().add("data:text/css;base64," + base64Css);
    }

    private void anchoTabla() {
        colId.prefWidthProperty().bind(tablaParadas.widthProperty().multiply(0.10));
        colNombre.prefWidthProperty().bind(tablaParadas.widthProperty().multiply(0.89));
    }

    @FXML
    void agregarParada(ActionEvent event) {
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            alerta("Error", "El nombre es obligatorio.", Alert.AlertType.WARNING);
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

        int id = Integer.parseInt(idStr);
        Transporte.getInstancia().editParada(id, nombre);

        updateTabla();
        cleanForm();
    }

    @FXML
    void eliminarParada(ActionEvent event) {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) return;

        int id = Integer.parseInt(idStr);
        String nombreParada = txtNombre.getText();

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminacion");
        confirmacion.setHeaderText("¿Esta seguro de que desea eliminar la parada: " + nombreParada + "?");
        confirmacion.setContentText("¡ADVERTENCIA! Eliminar esta parada borrará automáticamente TODAS las rutas (origen o destino) que estén conectadas a ella. Esta acción no se puede deshacer.");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                Transporte.getInstancia().deleteParada(id);
                updateTabla();
                cleanForm();
            } else {
                cleanForm();
            }
        });
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


    private void alerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
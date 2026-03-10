package com.example.algoritmosclasicosproyecto.controladores;

import com.example.algoritmosclasicosproyecto.logica.Parada;
import com.example.algoritmosclasicosproyecto.logica.Ruta;
import com.example.algoritmosclasicosproyecto.logica.Transporte;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class RutasController {

    @FXML private ComboBox<Parada> cmbOrigen;
    @FXML private ComboBox<Parada> cmbDestino;
    @FXML private TextField txtTiempo;
    @FXML private TextField txtDistancia;
    @FXML private TextField txtCosto;
    @FXML private CheckBox chkTrasbordo;
    @FXML private Button btnAgregar, btnEditar, btnEliminar;

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
        configurarColumnasTabla();
        actualizarTabla();

        // Listener de selección sin lambdas
        tablaRutas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ruta>() {
            @Override
            public void changed(ObservableValue<? extends Ruta> obs, Ruta viejaSeleccion, Ruta nuevaSeleccion) {
                if (nuevaSeleccion != null) {
                    cmbOrigen.setValue(nuevaSeleccion.getOrigen());
                    cmbDestino.setValue(nuevaSeleccion.getDestino());
                    txtTiempo.setText(String.valueOf(nuevaSeleccion.getTiempoMinuto()));
                    txtDistancia.setText(String.valueOf(nuevaSeleccion.getDistanciaKm()));
                    txtCosto.setText(String.valueOf(nuevaSeleccion.getCosto()));
                    chkTrasbordo.setSelected(nuevaSeleccion.isRequiereTrasbordo());

                    // Bloquear claves primarias y botón agregar
                    cmbOrigen.setDisable(true);
                    cmbDestino.setDisable(true);
                    btnAgregar.setDisable(true);
                    btnEditar.setDisable(false);
                    btnEliminar.setDisable(false);
                }
            }
        });

        limpiarFormulario();
    }

    private void configurarColumnasTabla() {
        colOrigen.setCellValueFactory(new Callback<CellDataFeatures<Ruta, String>, ObservableValue<String>>() {
            @Override public ObservableValue<String> call(CellDataFeatures<Ruta, String> p) { return new SimpleStringProperty(p.getValue().getOrigen().getNombre()); }
        });
        colDestino.setCellValueFactory(new Callback<CellDataFeatures<Ruta, String>, ObservableValue<String>>() {
            @Override public ObservableValue<String> call(CellDataFeatures<Ruta, String> p) { return new SimpleStringProperty(p.getValue().getDestino().getNombre()); }
        });
        colTiempo.setCellValueFactory(new Callback<CellDataFeatures<Ruta, Double>, ObservableValue<Double>>() {
            @Override public ObservableValue<Double> call(CellDataFeatures<Ruta, Double> p) {
                return new SimpleObjectProperty<>(p.getValue().getTiempoMinuto());
            }
        });
        colDistancia.setCellValueFactory(new Callback<CellDataFeatures<Ruta, Double>, ObservableValue<Double>>() {
            @Override public ObservableValue<Double> call(CellDataFeatures<Ruta, Double> p) {
                return new SimpleObjectProperty<>(p.getValue().getDistanciaKm());
            }
        });
        colCosto.setCellValueFactory(new Callback<CellDataFeatures<Ruta, Double>, ObservableValue<Double>>() {
            @Override public ObservableValue<Double> call(CellDataFeatures<Ruta, Double> p) {
                return new SimpleObjectProperty<>(p.getValue().getCosto());
            }
        });
        colTrasbordo.setCellValueFactory(new Callback<CellDataFeatures<Ruta, Boolean>, ObservableValue<Boolean>>() {
            @Override public ObservableValue<Boolean> call(CellDataFeatures<Ruta, Boolean> p) { return new SimpleObjectProperty<>(p.getValue().isRequiereTrasbordo()); }
        });
    }

    private void actualizarTabla() {
        // Asegúrate de usar getTodasLasParadas() si getParadas() da error
        ObservableList<Parada> paradas = FXCollections.observableArrayList(Transporte.getInstancia().getParadas());
        cmbOrigen.setItems(paradas);
        cmbDestino.setItems(paradas);

        ObservableList<Ruta> rutas = FXCollections.observableArrayList(Transporte.getInstancia().getRutas());
        tablaRutas.setItems(rutas);
        tablaRutas.refresh();
    }

    @FXML
    void agregarRuta(ActionEvent event) {
        if (!validarFormulario()) return;

        Parada origen = cmbOrigen.getValue();
        Parada destino = cmbDestino.getValue();

        Transporte.getInstancia().addRuta(origen.getId(), destino.getId(),
                Double.parseDouble(txtTiempo.getText().trim()),
                Double.parseDouble(txtDistancia.getText().trim()),
                Double.parseDouble(txtCosto.getText().trim()),
                chkTrasbordo.isSelected());

        actualizarTabla();
        limpiarFormulario();
    }

    @FXML
    void editarRuta(ActionEvent event) {
        if (!validarFormulario()) return;

        Parada origen = cmbOrigen.getValue();
        Parada destino = cmbDestino.getValue();

        Transporte.getInstancia().editRuta(origen.getId(), destino.getId(),
                Double.parseDouble(txtTiempo.getText().trim()),
                Double.parseDouble(txtDistancia.getText().trim()),
                Double.parseDouble(txtCosto.getText().trim()),
                chkTrasbordo.isSelected());

        actualizarTabla();
        limpiarFormulario();
    }

    @FXML
    void eliminarRuta(ActionEvent event) {
        Parada origen = cmbOrigen.getValue();
        Parada destino = cmbDestino.getValue();

        if (origen != null && destino != null) {
            Transporte.getInstancia().deleteRuta(origen.getId(), destino.getId());
            actualizarTabla();
            limpiarFormulario();
        }
    }

    @FXML
    void limpiarFormularioAction(ActionEvent event) {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        cmbOrigen.getSelectionModel().clearSelection();
        cmbDestino.getSelectionModel().clearSelection();
        txtTiempo.clear();
        txtDistancia.clear();
        txtCosto.clear();
        chkTrasbordo.setSelected(false);

        cmbOrigen.setDisable(false);
        cmbDestino.setDisable(false);
        btnAgregar.setDisable(false);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
        tablaRutas.getSelectionModel().clearSelection();
    }

    private boolean validarFormulario() {
        Parada origen = cmbOrigen.getValue();
        Parada destino = cmbDestino.getValue();

        if (origen == null || destino == null) {
            mostrarAlerta("Error", "Debe seleccionar origen y destino.");
            return false;
        }
        if (origen.getId().equals(destino.getId())) {
            mostrarAlerta("Error de Lógica", "El origen y el destino no pueden ser iguales.");
            return false;
        }

        try {
            double tiempo = Double.parseDouble(txtTiempo.getText().trim());
            double distancia = Double.parseDouble(txtDistancia.getText().trim());
            double costo = Double.parseDouble(txtCosto.getText().trim());

            if (tiempo <= 0 || distancia <= 0 || costo < 0) {
                mostrarAlerta("Datos Inválidos", "Tiempo y distancia deben ser > 0. El costo no puede ser negativo.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "Ingrese valores numéricos válidos en tiempo, distancia y costo.");
            return false;
        }
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
            @Override public String toString(Parada parada) { return parada == null ? "" : parada.getNombre(); }
            @Override public Parada fromString(String string) { return null; }
        };
        cmbOrigen.setConverter(converter);
        cmbDestino.setConverter(converter);
    }
}
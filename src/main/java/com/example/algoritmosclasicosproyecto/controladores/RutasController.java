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
    @FXML private TextField txtTrasbordo;
    @FXML private Button btnAgregar, btnEditar, btnEliminar;

    @FXML private TableView<Ruta> tablaRutas;
    @FXML private TableColumn<Ruta, String> colOrigen;
    @FXML private TableColumn<Ruta, String> colDestino;
    @FXML private TableColumn<Ruta, Double> colTiempo;
    @FXML private TableColumn<Ruta, Double> colDistancia;
    @FXML private TableColumn<Ruta, Double> colCosto;
    @FXML private TableColumn<Ruta, Integer> colTrasbordo;

    @FXML
    public void initialize() {
   /*     comboList();
        tabla();
        actualizarTabla();


        tablaRutas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ruta>() {
            @Override
            public void changed(ObservableValue<? extends Ruta> obs, Ruta antigua, Ruta nueva) {
                if (nueva != null) {
                    cmbOrigen.setValue(nueva.getOrigen());
                    cmbDestino.setValue(nueva.getDestino());
                    txtTiempo.setText(String.valueOf(nueva.getTiempo()));
                    txtDistancia.setText(String.valueOf(nueva.getDistancia()));
                    txtCosto.setText(String.valueOf(nueva.getCosto()));
                    txtTrasbordo.setText(String.valueOf(Integer.valueOf((int) nueva.getTrasbordo())));



                    cmbOrigen.setDisable(true);
                    cmbDestino.setDisable(true);
                    btnAgregar.setDisable(true);
                    btnEditar.setDisable(false);
                    btnEliminar.setDisable(false);
                }
            }
        });

        cleanform();
    }

    private void tabla() {
        colOrigen.setCellValueFactory(new Callback<CellDataFeatures<Ruta, String>, ObservableValue<String>>() {
            @Override public ObservableValue<String> call(CellDataFeatures<Ruta, String> p) { return new SimpleStringProperty(p.getValue().getOrigen().getNombre()); }
        });
        colDestino.setCellValueFactory(new Callback<CellDataFeatures<Ruta, String>, ObservableValue<String>>() {
            @Override public ObservableValue<String> call(CellDataFeatures<Ruta, String> p) { return new SimpleStringProperty(p.getValue().getDestino().getNombre()); }
        });
        colTiempo.setCellValueFactory(new Callback<CellDataFeatures<Ruta, Double>, ObservableValue<Double>>() {
            @Override public ObservableValue<Double> call(CellDataFeatures<Ruta, Double> p) {
                return new SimpleObjectProperty<>(p.getValue().getTiempo());
            }
        });
        colDistancia.setCellValueFactory(new Callback<CellDataFeatures<Ruta, Double>, ObservableValue<Double>>() {
            @Override public ObservableValue<Double> call(CellDataFeatures<Ruta, Double> p) {
                return new SimpleObjectProperty<>(p.getValue().getDistancia());
            }
        });
        colCosto.setCellValueFactory(new Callback<CellDataFeatures<Ruta, Double>, ObservableValue<Double>>() {
            @Override public ObservableValue<Double> call(CellDataFeatures<Ruta, Double> p) {
                return new SimpleObjectProperty<>(p.getValue().getCosto());
            }
        });
        colTrasbordo.setCellValueFactory(new Callback<CellDataFeatures<Ruta, Integer>, ObservableValue<Integer>>() {
            @Override public SimpleObjectProperty<Integer> call(CellDataFeatures<Ruta, Integer> p) {
                return new SimpleObjectProperty<Integer>((int) p.getValue().getTrasbordo());
            }
        });
    }

    private void actualizarTabla() {

        ObservableList<Parada> paradas = FXCollections.observableArrayList(Transporte.getInstancia().getParadas());
        cmbOrigen.setItems(paradas);
        cmbDestino.setItems(paradas);

        ObservableList<Ruta> rutas = FXCollections.observableArrayList(Transporte.getInstancia().getRutas());
        tablaRutas.setItems(rutas);
        tablaRutas.refresh();
    }

    @FXML
    void agregarRuta(ActionEvent event) {
        if (!checkForm()) return;

        Parada origen = cmbOrigen.getValue();
        Parada destino = cmbDestino.getValue();

        Transporte.getInstancia().addRuta(origen.getId(), destino.getId(),
                Double.parseDouble(txtTiempo.getText().trim()),
                Double.parseDouble(txtDistancia.getText().trim()),
                Double.parseDouble(txtCosto.getText().trim()),
                Integer.parseInt(txtTrasbordo.getText().trim()));

        actualizarTabla();
        cleanform();
    }

    @FXML
    void editarRuta(ActionEvent event) {
        if (!checkForm()) return;

        Parada origen = cmbOrigen.getValue();
        Parada destino = cmbDestino.getValue();

        Transporte.getInstancia().editRuta(origen.getId(), destino.getId(),
                Double.parseDouble(txtTiempo.getText().trim()),
                Double.parseDouble(txtDistancia.getText().trim()),
                Double.parseDouble(txtCosto.getText().trim()),
                Integer.parseInt(txtTrasbordo.getText().trim()));

        actualizarTabla();
        cleanform();
    }

    @FXML
    void eliminarRuta(ActionEvent event) {
        Parada origen = cmbOrigen.getValue();
        Parada destino = cmbDestino.getValue();

        if (origen != null && destino != null) {
            Transporte.getInstancia().deleteRuta(origen.getId(), destino.getId());
            actualizarTabla();
            cleanform();
        }
    }

    @FXML
    void limpiarFormularioAction(ActionEvent event) {
        cleanform();
    }

    private void cleanform() {
        cmbOrigen.getSelectionModel().clearSelection();
        cmbDestino.getSelectionModel().clearSelection();
        txtTiempo.clear();
        txtDistancia.clear();
        txtCosto.clear();
        txtTrasbordo.clear();

        cmbOrigen.setDisable(false);
        cmbDestino.setDisable(false);
        btnAgregar.setDisable(false);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
        tablaRutas.getSelectionModel().clearSelection();
    }

    private boolean checkForm() {
        Parada origen = cmbOrigen.getValue();
        Parada destino = cmbDestino.getValue();

        if (origen == null || destino == null) {
            alerta("Error", "Debe seleccionar origen y destino.");
            return false;
        }
        if (origen.getId().equals(destino.getId())) {
            alerta("Error de Lógica", "El origen y el destino no pueden ser iguales.");
            return false;
        }

        try {
            double tiempo = Double.parseDouble(txtTiempo.getText().trim());
            double distancia = Double.parseDouble(txtDistancia.getText().trim());
            double costo = Double.parseDouble(txtCosto.getText().trim());

            if (tiempo <= 0 || distancia <= 0 || costo < 0) {
                alerta("Datos Inválidos", "Tiempo y distancia deben ser > 0. El costo no puede ser negativo.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            alerta("Error de Formato", "Ingrese valores validos.");
            return false;
        }
    }

    private void alerta(String titulo, String contenido) {
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
    */}
}
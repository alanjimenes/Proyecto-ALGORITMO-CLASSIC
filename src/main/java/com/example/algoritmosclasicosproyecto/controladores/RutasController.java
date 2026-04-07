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
import javafx.scene.control.TextFormatter;
import javafx.util.Callback;
import javafx.util.StringConverter;
import java.util.function.UnaryOperator;

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
        configComboBoxes();
        configTabla();
        actualizarDatos();
        aplicarFiltrosNumericos();

        tablaRutas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ruta>() {
            @Override
            public void changed(ObservableValue<? extends Ruta> obs, Ruta antigua, Ruta nueva) {
                if (nueva != null) {
                    cmbOrigen.setValue(nueva.getOrigen());
                    cmbDestino.setValue(nueva.getDestino());
                    txtTiempo.setText(String.valueOf(nueva.getTiempo()));
                    txtDistancia.setText(String.valueOf(nueva.getDistancia()));
                    txtCosto.setText(String.valueOf(nueva.getCosto()));
                    txtTrasbordo.setText(String.valueOf((int) nueva.getTrasbordo()));

                    cmbOrigen.setDisable(true);
                    cmbDestino.setDisable(true);

                    btnAgregar.setDisable(true);
                    btnEditar.setDisable(false);
                    btnEliminar.setDisable(false);
                } else {
                    cleanform();
                }
            }
        });

        cleanform();
        inyeccionCss();
        anchoTabla();
    }

    private void aplicarFiltrosNumericos() {
        String regexDecimal = "^[0-9]*\\.?[0-9]*$";
        String regexEntero = "^[0-9]*$";

        UnaryOperator<TextFormatter.Change> filtroDecimal = new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                String nuevoTexto = change.getControlNewText();
                if (nuevoTexto.matches(regexDecimal)) {
                    return change;
                }
                return null;
            }
        };

        UnaryOperator<TextFormatter.Change> filtroEntero = new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                String nuevoTexto = change.getControlNewText();
                if (nuevoTexto.matches(regexEntero)) {
                    return change;
                }
                return null;
            }
        };

        txtTiempo.setTextFormatter(new TextFormatter<>(filtroDecimal));
        txtDistancia.setTextFormatter(new TextFormatter<>(filtroDecimal));
        txtCosto.setTextFormatter(new TextFormatter<>(filtroDecimal));

        txtTrasbordo.setTextFormatter(new TextFormatter<>(filtroEntero));
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
        tablaRutas.getStylesheets().add("data:text/css;base64," + base64Css);
    }
    private void anchoTabla(){
        colOrigen.prefWidthProperty().bind(tablaRutas.widthProperty().multiply(0.20));
        colDestino.prefWidthProperty().bind(tablaRutas.widthProperty().multiply(0.20));
        colTiempo.prefWidthProperty().bind(tablaRutas.widthProperty().multiply(0.15));
        colDistancia.prefWidthProperty().bind(tablaRutas.widthProperty().multiply(0.15));
        colCosto.prefWidthProperty().bind(tablaRutas.widthProperty().multiply(0.15));
        colTrasbordo.prefWidthProperty().bind(tablaRutas.widthProperty().multiply(0.14));
    }

    private void configTabla() {
        colOrigen.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ruta, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Ruta, String> p) {
                return new SimpleStringProperty(p.getValue().getOrigen().getNombre());
            }
        });

        colDestino.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ruta, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Ruta, String> p) {
                return new SimpleStringProperty(p.getValue().getDestino().getNombre());
            }
        });

        colTiempo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ruta, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Ruta, Double> p) {
                return new SimpleObjectProperty<>(p.getValue().getTiempo());
            }
        });

        colDistancia.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ruta, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Ruta, Double> p) {
                return new SimpleObjectProperty<>(p.getValue().getDistancia());
            }
        });

        colCosto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ruta, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Ruta, Double> p) {
                return new SimpleObjectProperty<>(p.getValue().getCosto());
            }
        });

        colTrasbordo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ruta, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Ruta, Integer> p) {
                return new SimpleObjectProperty<>((int) p.getValue().getTrasbordo());
            }
        });
    }

    private void actualizarDatos() {
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

        Transporte.getInstancia().addRuta(
                origen.getId(),
                destino.getId(),
                Double.parseDouble(txtTiempo.getText().trim()),
                Double.parseDouble(txtDistancia.getText().trim()),
                Double.parseDouble(txtCosto.getText().trim()),
                Integer.parseInt(txtTrasbordo.getText().trim())
        );

        actualizarDatos();
        cleanform();
    }

    @FXML
    void editarRuta(ActionEvent event) {
        if (!checkForm()) return;

        Parada origen = cmbOrigen.getValue();
        Parada destino = cmbDestino.getValue();

        Transporte.getInstancia().editRuta(
                origen.getId(),
                destino.getId(),
                Double.parseDouble(txtTiempo.getText().trim()),
                Double.parseDouble(txtDistancia.getText().trim()),
                Double.parseDouble(txtCosto.getText().trim()),
                Integer.parseInt(txtTrasbordo.getText().trim())
        );

        actualizarDatos();
        cleanform();
        tablaRutas.getSelectionModel().clearSelection();
    }

    @FXML
    void eliminarRuta(ActionEvent event) {
        Parada origen = cmbOrigen.getValue();
        Parada destino = cmbDestino.getValue();

        if (origen != null && destino != null) {
            Transporte.getInstancia().deleteRuta(origen.getId(), destino.getId());
            actualizarDatos();
            cleanform();
        }
    }

    @FXML
    void limpiarFormularioAction(ActionEvent event) {
        cleanform();
        tablaRutas.getSelectionModel().clearSelection();
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
    }

    private boolean checkForm() {
        Parada origen = cmbOrigen.getValue();
        Parada destino = cmbDestino.getValue();

        if (origen == null || destino == null) {
            alert("Error de Selección", "Debe seleccionar un origen y un destino válidos.");
            return false;
        }

        if (origen.getId() == destino.getId()) {
            alert("Error de Lógica", "El origen y el destino no pueden ser la misma parada.");
            return false;
        }

        if (txtTiempo.getText().trim().isEmpty() || txtDistancia.getText().trim().isEmpty() ||
                txtCosto.getText().trim().isEmpty() || txtTrasbordo.getText().trim().isEmpty()) {
            alert("Campos Vacíos", "Todos los campos numéricos deben ser completados.");
            return false;
        }

        try {
            double tiempo = Double.parseDouble(txtTiempo.getText().trim());
            double distancia = Double.parseDouble(txtDistancia.getText().trim());
            double costo = Double.parseDouble(txtCosto.getText().trim());
            int trasbordo = Integer.parseInt(txtTrasbordo.getText().trim());

            if (tiempo <= 0 || distancia <= 0 || costo < 0) {
                alert("Datos Inválidos", "El tiempo y la distancia deben ser mayores a 0. El costo no puede ser negativo.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            alert("Error de Formato", "Ingrese valores numéricos válidos. Asegúrese de no dejar solo un punto (ej: '.').");
            return false;
        }
    }

    private void alert(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    private void configComboBoxes() {
        StringConverter<Parada> converter = new StringConverter<Parada>() {
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
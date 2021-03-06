package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import controllers.utils.Messages;
import controllers.utils.Validators;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import modelo.Conexion;
import modelo.rollo.Rollo;
import tray.notification.NotificationType;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class MaderaEnRolloController implements Initializable {

    @FXML private VBox panel;
    @FXML private JFXTextField txtD1;
    @FXML private JFXTextField txtD2;
    @FXML private JFXComboBox <String> comboTamaño;
    @FXML private JFXTextField volumenTotal;
    @FXML private JFXDatePicker fecha;
    @FXML private JFXButton botonAgregar;
    @FXML private JFXButton botonEliminar;
    @FXML private JFXButton botonBuscar;
    @FXML private JFXButton botonFechaActual;
    @FXML private Label lblTitulo;
    @FXML private JFXTreeTableView<Rollo> tabla1;

    private ObservableList<Rollo> list;
    private Conexion conexion = Conexion.getInstance();

    @FXML
    void entradaAgregar(KeyEvent event) {

    }

    @FXML
    void agregaRollo(ActionEvent event) {
            agregarRegistro();
            txtD1.setText("");
            txtD2.setText("");

            var total = list.parallelStream().mapToDouble(Rollo::getVol).sum();
            volumenTotal.setText(format3Decimals(total) + "");
    }

    @FXML
    void returnFecha(ActionEvent event) {
        list.removeIf(x -> true);

        conexion.establecerConexion();
        Rollo.obtenerDatos(conexion.getConection(), list);
        conexion.cerrarConexion();

        txtD1.setDisable(false);
        txtD2.setDisable(false);
        botonAgregar.setDisable(false);
        botonEliminar.setDisable(false);
        comboTamaño.setDisable(false);


        var total = list.parallelStream().mapToDouble(Rollo::getVol).sum();
        volumenTotal.setText(format3Decimals(total) + "");

        lblTitulo.setText("Control de transformacion de madera en rollo.");

        fecha.getEditor().clear();
        fecha.setValue(null);

        Messages.setMessage("Dia actual", "Ahora visualiza la fecha actual", NotificationType.SUCCESS);
    }

    @FXML
    void eliminaRollo(ActionEvent event) {
        try {
            int row = tabla1.getSelectionModel().getSelectedItem().getValue().getId();
            conexion.establecerConexion();
            Rollo.eliminarRollo(conexion.getConection(), row);
            list.removeIf(x -> true);
            Rollo.obtenerDatos(conexion.getConection(), list);
            conexion.cerrarConexion();

            var total = list.parallelStream().mapToDouble(Rollo::getVol).sum();
            volumenTotal.setText(format3Decimals(total) + "");

            Messages.setMessage("Se elimino", "El rollo se elimino satisfactoriamente", NotificationType.SUCCESS);
        } catch (Exception e) {
            Messages.setMessage("Error", "No se selecciono una fila", NotificationType.ERROR);
        }

    }

    @FXML
    void buscarFecha(ActionEvent event) {
        try {
            var datePicker1 = fecha.getValue().getYear() + "-" + fecha.getValue().getMonthValue() + "-" + fecha.getValue().getDayOfMonth();
            lblTitulo.setText("Registro del: " + fecha.getValue().getDayOfMonth() + "/" + fecha.getValue().getMonth() + "/" + fecha.getValue().getYear()
            );

            cargarDatos(datePicker1);

            txtD2.setDisable(true);
            txtD1.setDisable(true);
            botonAgregar.setDisable(true);
            botonEliminar.setDisable(true);
            comboTamaño.setDisable(true);

            var total = list.parallelStream().mapToDouble(Rollo::getVol).sum();
            volumenTotal.setText(format3Decimals(total) + "");

            Messages.setMessage("Registros", "Ahora visualiza una fecha distinta a la actual", NotificationType.SUCCESS);

        } catch (Exception e) {
            Messages.setMessage("Error", "No selecciono una fecha", NotificationType.ERROR);
        }

    }

    private void cargarDatos(String datePicker) {
        list.removeIf(x -> true);
        conexion.establecerConexion();
        Rollo.historial(conexion.getConection(), list, datePicker);
        conexion.cerrarConexion();
    }

    private double format3Decimals(double numero) {
        NumberFormat d = new DecimalFormat("#0.000");
        var f = d.format(numero);
        return Double.parseDouble(f);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        list = FXCollections.observableArrayList();
        comboTamaño.getItems().addAll("8\" 1/4", "16\" 1/2");
        comboTamaño.setValue("8\" 1/4");
        columnas();
        conexion.establecerConexion();
        Rollo.obtenerDatos(conexion.getConection(), list);
        conexion.cerrarConexion();

        var total = list.parallelStream().mapToDouble(Rollo::getVol).sum();
        volumenTotal.setText(format3Decimals(total) + "");


        botonAgregar.setTooltip(new Tooltip("Agregar"));
        botonBuscar.setTooltip(new Tooltip("Buscar"));
        botonEliminar.setTooltip(new Tooltip("Eliminar"));
        botonFechaActual.setTooltip(new Tooltip("Regrese al dia actual"));

        panel.addEventHandler(KeyEvent.ANY, x -> {
            if (x.getCode().getCode() == 10) {
                agregarRegistro();
                txtD1.setText("");
                txtD2.setText("");

                var total1 = list.parallelStream().mapToDouble(Rollo::getVol).sum();
                volumenTotal.setText(format3Decimals(total1) + "");
            }
        });
    }

    private void columnas() {
        final TreeItem<Rollo> root = new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren);
        list.forEach(x -> System.out.println(x));
        tabla1.setRoot(root);

        // Columna de numero de rollo.
        JFXTreeTableColumn<Rollo, Integer> numero = new JFXTreeTableColumn<>("#Numero");
        numero.setEditable(false);

        numero.setCellValueFactory((TreeTableColumn.CellDataFeatures<Rollo, Integer> param) -> {
            if (numero.validateValue(param))
                return param.getValue().getValue().numProperty().asObject();
            else
                return numero.getComputedValue(param);
        });

        // Columna diametro1.
        JFXTreeTableColumn<Rollo, Double> diametro1 = new JFXTreeTableColumn<>("Diametro 1");
        diametro1.setEditable(false);

        diametro1.setCellValueFactory((TreeTableColumn.CellDataFeatures<Rollo, Double> param) -> {
            if (diametro1.validateValue(param))
                return param.getValue().getValue().d1Property().asObject();
            else
                return diametro1.getComputedValue(param);
        });

        // Columna diametro2.
        JFXTreeTableColumn<Rollo, Double> diametro2 = new JFXTreeTableColumn<>("Diametro 2");
        diametro2.setEditable(false);

        diametro2.setCellValueFactory((TreeTableColumn.CellDataFeatures<Rollo, Double> param) -> {
            if (diametro2.validateValue(param))
                return param.getValue().getValue().d2Property().asObject();
            else
                return diametro2.getComputedValue(param);
        });

        // Columna de diametro promedio.
        JFXTreeTableColumn<Rollo, Double> diametropromedio = new JFXTreeTableColumn<>("Diametro promedio");
        diametropromedio.setEditable(false);

        diametropromedio.setCellValueFactory((TreeTableColumn.CellDataFeatures<Rollo, Double> param) -> {
            if (diametropromedio.validateValue(param))
                return param.getValue().getValue().dtProperty().asObject();
            else
                return diametropromedio.getComputedValue(param);
        });

        // Columna de tamaño.
        JFXTreeTableColumn<Rollo, String> tamanio = new JFXTreeTableColumn<>("Tamaño");
        tamanio.setEditable(false);

        tamanio.setCellValueFactory((TreeTableColumn.CellDataFeatures<Rollo, String> param) -> {
            if (tamanio.validateValue(param))
                return param.getValue().getValue().tamProperty();
            else
                return tamanio.getComputedValue(param);
        });

        // Columna de volumen.
        JFXTreeTableColumn<Rollo, Double> volumen = new JFXTreeTableColumn<>("Volumen");
        volumen.setEditable(false);

        volumen.setCellValueFactory((TreeTableColumn.CellDataFeatures<Rollo, Double> param) -> {
            if (volumen.validateValue(param))
                return param.getValue().getValue().volProperty().asObject();
            else
                return volumen.getComputedValue(param);
        });

        //Operaciones con la tabla
        tabla1.setEditable(false);
        tabla1.setShowRoot(false);
        tabla1.getColumns().setAll(numero, diametro1, diametro2, diametropromedio, tamanio, volumen);
    }

    public void agregarRegistro(){
        String combito = comboTamaño.getSelectionModel().getSelectedItem();
        double valorTam = 0;

        if (combito == "8\" 1/4"){
            valorTam = 8.25;
        } else if (combito == "16\" 1/2"){
            valorTam = 16.5;
        }

        try {
            conexion.establecerConexion();
            var newRollo = Rollo.addRollo(conexion.getConection(), Double.parseDouble(txtD1.getText()), Double.parseDouble(txtD2.getText()), valorTam);

            conexion.cerrarConexion();
            if (newRollo != null) {
                list.add(newRollo);
                Messages.setMessage("Agregado", "Se agrego el rollo", NotificationType.SUCCESS);
            }
        }catch (Exception e ) {
            txtD1.setText("");
            txtD2.setText("");
            Messages.setMessage("Error", "Ambos campos deben contener datos validos", NotificationType.ERROR);

        }

    }
}

package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import modelo.Conexion;
import modelo.Control_madera.madera_control;
import modelo.otros_madera.otros_mad;

import java.net.URL;
import java.util.ResourceBundle;

public class otrosController implements Initializable {

    @FXML
    private VBox vBoxOtros;


    @FXML
    private VBox vBoxotros;

    @FXML
    private JFXComboBox<String> ComboPz;

    @FXML
    private JFXTreeTableView<otros_mad> tablaOtros;

    @FXML
    private JFXTextField txtCubicacion;

    @FXML
    private TextField txtPieza;

    @FXML
    private JFXTextField txtPt;

    double valcub;
    private ObservableList<otros_mad> list;

    private Conexion conexion = Conexion.getInstance();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ComboPz.getItems().addAll("POL 4X4","POL 3.5X3.5","POL 3X3","CUAD 2X4","CUAD 1.5X3.5","VIGA 4x4","VIGA 4x6","VIGA 4x8");
        ComboPz.setValue("POL 4X4");
        txtPt.setText("0");
        list = FXCollections.observableArrayList();
        txtCubicacion.setEditable(false);
        txtPt.setEditable(false);
        valcub=(4*4*8.25)/12;
        conexion.establecerConexion();
        otros_mad.obtenerDatos(conexion.getConection(), list);
        conexion.cerrarConexion();
        txtCubicacion.setText(String.valueOf(valcub));
        columns();

    }


    //Declaracion de la tabla y columnas
    //Columna1
    JFXTreeTableColumn<otros_mad, String> Columna1 = new JFXTreeTableColumn<>("OTRAS");
    //Columna2
    JFXTreeTableColumn<otros_mad, Integer> Columna2 = new JFXTreeTableColumn<otros_mad, Integer>("N PIEZA");
    //Columna3
    JFXTreeTableColumn<otros_mad, Double> Columna3 = new JFXTreeTableColumn<>("CUBICACION");
    //Columna4
    JFXTreeTableColumn<otros_mad, Double> Columna4 = new JFXTreeTableColumn<>("PT");

    private void columns() {
        final TreeItem<otros_mad> root = new RecursiveTreeItem<otros_mad>(list, RecursiveTreeObject::getChildren);
        tablaOtros.setRoot(root);

            //Columna1
            Columna1.setEditable(false);
            Columna1.setCellValueFactory((TreeTableColumn.CellDataFeatures<otros_mad, String> param) -> {
                if (Columna1.validateValue(param)) {
                    return param.getValue().getValue().selPiezaProperty();
                } else
                    return Columna1.getComputedValue(param);

            });
        //Columna2
        Columna2.setEditable(false);
        Columna2.setCellValueFactory((TreeTableColumn.CellDataFeatures<otros_mad, Integer> param) -> {
            if (Columna2.validateValue(param)) {
                return param.getValue().getValue().piezaProperty().asObject();
            } else
                return Columna2.getComputedValue(param);

        });

        //Columna3
        Columna3.setEditable(false);
        Columna3.setCellValueFactory((TreeTableColumn.CellDataFeatures<otros_mad, Double> param) -> {
            if (Columna3.validateValue(param)) {
                return param.getValue().getValue().cubicacionProperty().asObject();
            } else
                return Columna3.getComputedValue(param);

        });

        //Columna4
        Columna4.setEditable(false);
        Columna4.setCellValueFactory((TreeTableColumn.CellDataFeatures<otros_mad, Double> param) -> {
            if (Columna4.validateValue(param)) {
                return param.getValue().getValue().ptProperty().asObject();
            } else
                return Columna4.getComputedValue(param);

        });


        tablaOtros.setEditable(false);
        tablaOtros.setShowRoot(false);
        tablaOtros.getColumns().setAll(Columna1, Columna2,Columna3,Columna4);
    }


    public void agregarRegistro(otros_mad x){
        conexion.establecerConexion();
        var newOtros = otros_mad.addOtros(conexion.getConection(),x);
        conexion.cerrarConexion();
        System.out.println(newOtros==null);
        if (newOtros != null) {
           list.add(newOtros);
       }
    }

    public void updateOtros(otros_mad x){
        var actual = tablaOtros.getSelectionModel().getSelectedItem().getValue();


        conexion.establecerConexion();
        var success = otros_mad.updateOtros(conexion.getConection(),x);
        if(success != null){
            list.removeIf(l -> l.getId() == success.getId());
            list.add(success);
            tablaOtros.getSelectionModel().getSelectedIndex();

        }



    }



    public void CalPt(){
        try {
            double val1 = Double.parseDouble(txtCubicacion.getText());
            double val2 = Integer.parseInt(txtPieza.getText());
            double resultado = val1 * val2;
            txtPt.setText(String.valueOf(resultado));
        }catch (Exception e){

        }

    }

    void CalCubicacion(){
        if(ComboPz.getSelectionModel().getSelectedItem()=="POL 4X4"){
            valcub=(4*4*8.25)/12;
            txtCubicacion.setText(String.valueOf(valcub));
        }else if(ComboPz.getSelectionModel().getSelectedItem()=="POL 3.5X3.5"){
            valcub=(3.5*3.5*8.25)/12;
            txtCubicacion.setText(String.valueOf(valcub));
        }else if(ComboPz.getSelectionModel().getSelectedItem()=="POL 3X3") {
            valcub = (3 * 3 * 8.25) / 12;
            txtCubicacion.setText(String.valueOf(valcub));
        }else if(ComboPz.getSelectionModel().getSelectedItem()=="CUAD 2X4"){
            valcub=(2*4*8.25)/12;
            txtCubicacion.setText(String.valueOf(valcub));
        }else if(ComboPz.getSelectionModel().getSelectedItem()=="CUAD 1.5X3.5"){
            valcub=(1.5*3.5*8.25)/12;
            txtCubicacion.setText(String.valueOf(valcub));
        }else if(ComboPz.getSelectionModel().getSelectedItem()=="VIGA 4x4"){
            valcub=(4*4*8.25)/12;
            txtCubicacion.setText(String.valueOf(valcub));
        }else if(ComboPz.getSelectionModel().getSelectedItem()=="VIGA 4x6"){
            valcub=(4*6*8.25)/12;
            txtCubicacion.setText(String.valueOf(valcub));
        }else if(ComboPz.getSelectionModel().getSelectedItem()=="VIGA 4x8"){
            valcub=(4*8*8.25)/12;
            txtCubicacion.setText(String.valueOf(valcub));
        }


    }



    @FXML
    void ActionComboPz(ActionEvent event) {
        CalCubicacion();
        CalPt();
    }


    @FXML
    void ActionPieza(KeyEvent event) {
        CalCubicacion();
        if(txtPieza.getText().equals("")){
            txtPt.setText("0");
        }
        else{
            CalPt();
        }
    }


    @FXML
    void addOtros(ActionEvent event) {
        agregarRegistro(new otros_mad(ComboPz.getSelectionModel().getSelectedItem(),
                Integer.parseInt(txtPieza.getText()),Double.parseDouble(txtCubicacion.getText()),
                Double.parseDouble(txtPt.getText())));
    }

    @FXML
    void EditOtros(ActionEvent event) {
        updateOtros(new otros_mad(ComboPz.getSelectionModel().getSelectedItem(),
                Integer.parseInt(txtPieza.getText()),Double.parseDouble(txtCubicacion.getText()),
                Double.parseDouble(txtPt.getText())));

    }

    public void camposEditar(otros_mad otros){

        //otros.set


    }

    @FXML
    void deleteOtros(ActionEvent event) {
        int row = tablaOtros.getSelectionModel().getSelectedItem().getValue().getId();
        conexion.establecerConexion();
        otros_mad.eliminarOtros(conexion.getConection(), row);
        conexion.cerrarConexion();
        list.removeIf(x -> x.getId() == row);

    }




}
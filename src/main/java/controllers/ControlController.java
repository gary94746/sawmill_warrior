package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyEvent;
import modelo.Control_madera.madera_control;

import java.net.URL;
import java.util.ResourceBundle;

public class ControlController implements Initializable {

    @FXML
    private JFXComboBox<String> comboGr;
    @FXML
    private JFXComboBox<String> comboAnc;
    @FXML
    private JFXComboBox<String> comboClase;
    @FXML
    private JFXComboBox<String> ComboRegistro;
    @FXML
    private JFXTreeTableView <madera_control> tablaControl;
    @FXML
    private JFXTextField txtPiezas;
    @FXML
    private JFXTextField txtCubicacion;
    @FXML
    private JFXTextField txtPT;
    @FXML
    private JFXComboBox<String> comboLargo;

    double valcub;


    private ObservableList<madera_control> list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Customizacion de componentes
        ComboRegistro.getItems().addAll("3/4\"", "1 1/2\"", "2\"");
        ComboRegistro.setValue("3/4\"");

        comboGr.getItems().addAll("3/4\"", "1 1/2\"", "2\"");
        comboGr.setValue("3/4\"");

        comboAnc.getItems().addAll("4","6","8","10","12");
        comboAnc.setValue("4");

        comboClase.getItems().addAll("PRIMERA","SEGUNDA","TERCERA BUENA","TERCERA MALA","MADERA CRUZADA");
        comboClase.setValue("PRIMERA");

        comboLargo.getItems().addAll("3/4\"","16 1/2\"");
        comboLargo.setValue("3/4\"");

        valcub=(.75*4*8.25)/12;
        txtCubicacion.setText(String.valueOf(valcub));

        txtPT.setText("0");


        /**ClasesArray clasesArray = ClasesArray.getInstance();
        clasesArray.getArrayClases().forEach(x -> comboClase.getItems().add(x));

        GruesoArray gruesoArray = GruesoArray.getInstance();
        gruesoArray.getArrayList().forEach(y-> comboGr.getItems().add(y));
        comboGr.setValue(gruesoArray.getArrayList().get(0));


        AnchoArray anchoArray = AnchoArray.getInstance();
        anchoArray.getArrayList().forEach(j-> comboAnc.getItems().add(j));
        comboAnc.setValue(anchoArray.getArrayList().get(0));*/
        list = FXCollections.observableArrayList();
        columns();

    }
    //Declaracion de la tabla y columnas
    //Columna1
    JFXTreeTableColumn<madera_control, String> Columna1 = new JFXTreeTableColumn<>("CLASE");
    //Columna2
    JFXTreeTableColumn<madera_control, String> Columna2 = new JFXTreeTableColumn<>("LARGO");
    //Columna3
    JFXTreeTableColumn<madera_control, String> Columna3 = new JFXTreeTableColumn<>("GRUESO");
    //Columna4
    JFXTreeTableColumn<madera_control, String> Columna4 = new JFXTreeTableColumn<>("ANCHO");
    //Columna5
    JFXTreeTableColumn<madera_control, Integer> Columna5 = new JFXTreeTableColumn<>("PIEZAS");
    //Columna6
    JFXTreeTableColumn<madera_control, Double> Columna6 = new JFXTreeTableColumn<>("CUBICACION");
    //Columna7
    JFXTreeTableColumn<madera_control, Double> Columna7 = new JFXTreeTableColumn<>("PT");



    private void columns() {
        final TreeItem<madera_control> root = new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren);
        list.forEach(x -> System.out.println(x));
        tablaControl.setRoot(root);


        //INICIO COLUMNA 1
            Columna1.setEditable(false);
            Columna1.setCellValueFactory((TreeTableColumn.CellDataFeatures<madera_control, String> param) -> {
                if (Columna1.validateValue(param)) {
                    return param.getValue().getValue().claseProperty();
                } else
                    return Columna1.getComputedValue(param);

            });


        //FIN DE LA COLUMNA 1

        //INICIO COLUMNA 2
            Columna2.setEditable(false);
            Columna2.setCellValueFactory((TreeTableColumn.CellDataFeatures<madera_control, String> param) -> {
                if (Columna2.validateValue(param)) {
                    return param.getValue().getValue().largoProperty();
                } else
                    return Columna2.getComputedValue(param);
            });


            //FIN DE LA COLUMNA 2

        //INICIO COLUMNA 3
            Columna3.setEditable(false);
            Columna3.setCellValueFactory((TreeTableColumn.CellDataFeatures<madera_control, String> param) -> {
                if (Columna3.validateValue(param)) {
                    return param.getValue().getValue().gruesoProperty();
                } else
                    return Columna3.getComputedValue(param);
            });


        //INICIO COLUMNA 4
            Columna4.setEditable(false);
            Columna4.setCellValueFactory((TreeTableColumn.CellDataFeatures<madera_control, String> param) -> {
                if (Columna4.validateValue(param)) {
                    return param.getValue().getValue().anchoProperty();
                } else
                    return Columna4.getComputedValue(param);
            });


        //FIN DE LA COLUMNA 4

        //INICIO COLUMNA 5
            Columna5.setEditable(false);
            Columna5.setCellValueFactory((TreeTableColumn.CellDataFeatures<madera_control, Integer> param) -> {
                if (Columna5.validateValue(param)) {
                    return param.getValue().getValue().piezaProperty().asObject();
                } else
                    return Columna5.getComputedValue(param);
            });

        //FIN DE LA COLUMNA 5
        //INICIO COLUMNA 6
        Columna6.setEditable(false);
        Columna6.setCellValueFactory((TreeTableColumn.CellDataFeatures<madera_control, Double> param) -> {
            if (Columna6.validateValue(param)) {
                return param.getValue().getValue().cubicacionProperty().asObject();
            } else
                return Columna6.getComputedValue(param);
        });

        //FIN DE LA COLUMNA 6

        //INICIO COLUMNA 6
        Columna7.setEditable(false);
        Columna7.setCellValueFactory((TreeTableColumn.CellDataFeatures<madera_control, Double> param) -> {
            if (Columna7.validateValue(param)) {
                return param.getValue().getValue().ptProperty().asObject();
            } else
                return Columna7.getComputedValue(param);
        });

        //FIN DE LA COLUMNA 6
        //Operaciones con la tabla
        tablaControl.setEditable(false);
        tablaControl.setShowRoot(false);
        tablaControl.getColumns().setAll(Columna1, Columna2,Columna3,Columna4,Columna5,Columna6,Columna7);



    }

    public void agregarRegistro(madera_control x){
        list.add(x);

    }

    public void CalPt(){
        try {
            double val1 = Double.parseDouble(txtCubicacion.getText());
            double val2 = Integer.parseInt(txtPiezas.getText());
            double resultado = val1 * val2;
            txtPT.setText(String.valueOf(resultado));
        }catch (Exception e){

        }

    }

    @FXML
    void addControl(ActionEvent event) {
            agregarRegistro(new madera_control(comboGr.getSelectionModel().getSelectedItem(),
                    comboAnc.getSelectionModel().getSelectedItem(),comboClase.getSelectionModel().getSelectedItem(),
                    Integer.parseInt(txtPiezas.getText()),Double.parseDouble(txtCubicacion.getText()),
                    Double.parseDouble(txtPT.getText()),comboLargo.getSelectionModel().getSelectedItem()));
    }


    @FXML
    void actionAncho(ActionEvent event) {
        if(comboAnc.getSelectionModel().getSelectedItem()=="4"){
            valcub=(.75*4*8.25)/12;
            txtCubicacion.setText(String.valueOf(valcub));
        }else if(comboAnc.getSelectionModel().getSelectedItem()=="6"){
            valcub=(.75*6*8.25)/12;
            txtCubicacion.setText(String.valueOf(valcub));
        }else if(comboAnc.getSelectionModel().getSelectedItem()=="8"){
            valcub=(.75*8*8.25)/12;
            txtCubicacion.setText(String.valueOf(valcub));
        }else if(comboAnc.getSelectionModel().getSelectedItem()=="10"){
            valcub=(.75*10*8.25)/12;
            txtCubicacion.setText(String.valueOf(valcub));
        }else if(comboAnc.getSelectionModel().getSelectedItem()=="12"){
            valcub=(.75*12*8.25)/12;
            txtCubicacion.setText(String.valueOf(valcub));
        }
    }

    @FXML
    void ActionPieza(KeyEvent event) {
        if(txtPiezas.getText().equals("")){
            txtPT.setText("0");
        }else{
            CalPt();
        }


    }

    @FXML
    void ActionClase(ActionEvent event) {
        columns();

    }

}
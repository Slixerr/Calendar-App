/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import application.TimeSlot;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import referencias.modelo.Alumno;
import referencias.modelo.Tutoria;

/**
 * FXML Controller class
 *
 * @author silvi
 */
public class FXMLSubjectController implements Initializable {

    @FXML
    private ComboBox<?> comboSubject;
    @FXML
    private ListView<Alumno> listLV;
    @FXML
    private TextArea boxDescription;
    
    private boolean changed = false;
    @FXML
    private Label timeLabel;
    
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
    
    private ObservableList<Alumno> datos = null;
    @FXML
    private Button addButton;
    @FXML
    private ComboBox<?> comboStudents;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        addButton.setDisable(true);
        addButton.disableProperty().bind(comboStudents.promptTextProperty().isEmpty());
        
        ArrayList<Alumno> misdatos = new ArrayList<Alumno>();
        datos = FXCollections.observableArrayList(misdatos);
        listLV.setItems(datos);
        listLV.setCellFactory(cel -> new MiCelda());
    }
    
    private void actualizar(ActionEvent event) {
        changed = true;
        ((Stage) boxDescription.getScene().getWindow()).close();
    }

    private void cancelar(ActionEvent event) {
        ((Stage) boxDescription.getScene().getWindow()).close();
    }

    public boolean isChanged() {
        return changed;
    }

    public void initText(String s) {
        boxDescription.setText(s);
    }

    public String getText() {
        return boxDescription.getText();
    }


    @FXML
    private void addStudent(ActionEvent event) {
        if ((!comboStudents.getPromptText().isEmpty())
                && (comboStudents.getPromptText().trim().length() != 0)) {
            //datos.add(new Alumno(comboStudents.getPromptText());
            //comboStudents.clear();
            comboStudents.requestFocus();  //cambio del foco al textfield.
            listLV.refresh();
        }
    }

    @FXML
    private void cancelMethod(ActionEvent event) {
        ((Stage) boxDescription.getScene().getWindow()).close();
    }

    @FXML
    private void acceptMethod(ActionEvent event) {
        
    
    
    
    }
    
    public void setTimeLabel(TimeSlot start, TimeSlot end) {
        Tutoria tutoria = null;
        //timeLabel.textProperty().bind(Bindings.format("\s - \s", tutoria.inicioProperty(), ));
        timeLabel.setText(start.getStart().format(timeFormatter) + " - " + end.getEnd().format(timeFormatter));
    }
    
    public void startVariables(TimeSlot start, TimeSlot end) {
        setTimeLabel(start,end);
        Tutoria tutoria = new Tutoria();
//        tutoria.fechaProperty().bind()
//            
//        tutoria.anotacionesProperty().bind()
//    
    }
}

class MiCelda extends ListCell<Alumno>{ //código de clase

    @Override
    protected void updateItem(Alumno item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if(empty || item == null) {
            setText("");
        }
        else {
            setText(item.getNombre() + " " + item.getApellidos());
        }
    }
        
    
    }

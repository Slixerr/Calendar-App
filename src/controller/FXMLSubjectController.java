/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import application.AlumnoCell;
import application.TimeSlot;
import java.net.URL;
import java.time.Duration;
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
import referencias.accesoBD.AccesoBD;
import referencias.modelo.Alumno;
import referencias.modelo.Asignatura;
import referencias.modelo.Tutoria;

/**
 * FXML Controller class
 *
 * @author silvi
 */
public class FXMLSubjectController implements Initializable {

    @FXML
    private ComboBox<Asignatura> comboSubject;
    @FXML
    private ListView<Alumno> listLV;
    @FXML
    private TextArea boxDescription;
    
    @FXML
    private Label timeLabel;
    
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
    
    private ObservableList<Alumno> datos = null;
    @FXML
    private Button addButton;
    @FXML
    private ComboBox<Alumno> comboStudents;
    
    private Tutoria tutoria = new Tutoria();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        addButton.setDisable(true);
        addButton.disableProperty().bind(comboStudents.getEditor().textProperty().isEmpty());
        datos = tutoria.getAlumnos();
        listLV.setItems(datos);
        listLV.setCellFactory(cel -> new AlumnoCell());
        
        
        comboSubject.setItems(AccesoBD.getInstance().getTutorias().getAsignaturas());
        comboStudents.setItems(AccesoBD.getInstance().getTutorias().getAlumnosTutorizados());
    }
    
    private void cancelar(ActionEvent event) {
        ((Stage) boxDescription.getScene().getWindow()).close();
    }

    @FXML
    private void addStudent(ActionEvent event) {
        if ((!comboStudents.getEditor().getText().isEmpty())
                && (comboStudents.getEditor().getText().trim().length() != 0)
                ) {
            datos.add(comboStudents.getValue());
            comboStudents.getEditor().setText("");
            comboStudents.requestFocus();
            listLV.refresh();
        }
    }

    @FXML
    private void cancelMethod(ActionEvent event) {
        FXMLCalendarioController.setTutoria(null);
        ((Stage) boxDescription.getScene().getWindow()).close();
    }

    @FXML
    private void acceptMethod(ActionEvent event) {
        FXMLCalendarioController.setTutoria(tutoria);
        ((Stage) boxDescription.getScene().getWindow()).close();

    }
    
    public void setTimeLabel(TimeSlot start, TimeSlot end) {
        //timeLabel.textProperty().bind(Bindings.format("\s - \s", tutoria.inicioProperty(), ));
        timeLabel.setText(start.getStart().format(timeFormatter) + " - " + end.getEnd().format(timeFormatter));
    }
    
    public void startVariables(TimeSlot start, TimeSlot end) {
        setTimeLabel(start,end);
        
        tutoria.setFecha(start.getDate());
        tutoria.setEstado(Tutoria.EstadoTutoria.PEDIDA);
        tutoria.setInicio(start.getStart().toLocalTime());
        tutoria.setDuracion(Duration.between(start.getStart(), end.getEnd()));
        tutoria.anotacionesProperty().bind(boxDescription.textProperty());
        tutoria.asignaturaProperty().bind(comboSubject.valueProperty());
    }
}

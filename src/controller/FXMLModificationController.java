/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import application.SimpleAlumnoCell;
import application.TimeSlot;
import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
public class FXMLModificationController implements Initializable {

    @FXML
    private ListView<Alumno> listLV;
    @FXML
    private TextArea boxDescription;
    
    @FXML
    private Label timeLabel;
    
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
    
    private ObservableList<Alumno> datos = null;
    int lastFilteredAlumnosSize = 0;
        
    ObservableList<Alumno> listaAlumnos = AccesoBD.getInstance().getTutorias().getAlumnosTutorizados();
    ObservableList<Asignatura> listaAsignaturas = AccesoBD.getInstance().getTutorias().getAsignaturas();
    FilteredList<String> filteredAlumnos;
    
    @FXML
    private Button addButton;
    @FXML
    private ComboBox<String> comboStudents;
    
    private Tutoria tutoria = new Tutoria();
    @FXML
    private Label errorLabel;
    @FXML
    private Label subjectLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        filteredAlumnos = new FilteredList<>(
                FXCollections.observableArrayList(
                        listaAlumnos.stream().map(Alumno::toString).collect(Collectors.toList())
                ), p -> true);
        
        addButton.setDisable(true);
        addButton.disableProperty().bind(comboStudents.getEditor().textProperty().isEmpty());
        
        datos = tutoria.getAlumnos();
        listLV.setItems(datos);
        listLV.setCellFactory(c -> new SimpleAlumnoCell());
        listLV.refresh();
        
        
        comboStudents.setItems(filteredAlumnos);
        filteredAlumnosListeners();
        comboStudentsListeners();
    }

    private void comboStudentsListeners() {
        comboStudents.getEditor().textProperty().addListener((a,b,c) -> {
            comboStudents.setValue(c);
        });
        /*
        * this piece of code exists to mitigate a bug in javafx
        * sometimes when changing the amount of items through listeners
        * strange things will happen to the combobox.
        * This approach eliminates these errors at the cost of introducing
        * a flicker when writing (caused by repeated show/hide of the box)
        * this is reduced by only flickering when the list size changes.
        */
        comboStudents.getItems().addListener((Observable c) -> {   
            Platform.runLater(() -> {
                if(filteredAlumnos.size() == 0) {
                    comboStudents.hide();
                }
                else if (lastFilteredAlumnosSize == filteredAlumnos.size()){
                    comboStudents.show();
                } else {
                    lastFilteredAlumnosSize = filteredAlumnos.size();
                    comboStudents.hide();
                    comboStudents.show();
                }
            });
        });
    }
    
    private void cancelar(ActionEvent event) {
        ((Stage) boxDescription.getScene().getWindow()).close();
    }

    @FXML
    private void addStudent(ActionEvent event) {
        Alumno alumno = checkMemberOf(comboStudents.getValue());
        
        if(alumno == null) {
            String[] names = comboStudents.getValue().split(" ");
            String surname = "";
            for (int i = 1;i<names.length;i++) surname = String.join(surname, names[i]);
            alumno = FXMLCalendarioController.createAlumno(names[0], surname);
            if (alumno == null) return;
            List<Alumno> alumnos= AccesoBD.getInstance().getTutorias().getAlumnosTutorizados();
            if (!alumnos.contains(alumno)) alumnos.add(alumno);
        }
        
        if (datos.contains(alumno) ) {
            errorLabel.setText("No se pueden añadir alumnos repetidos.");
        } else if (!comboStudents.getValue().equals("")) {
            datos.add(alumno);
            listLV.refresh();
        }
        comboStudents.getEditor().setText("");
        comboStudents.getSelectionModel().clearSelection();
        comboStudents.requestFocus();
    }

    private Alumno checkMemberOf(String nombreAlumno){
        return listaAlumnos.stream().filter((Alumno a) -> {
            return a.toString().equalsIgnoreCase(nombreAlumno);
        }).findAny().orElse(null);
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
    
    /*
    *  runlater needed as text property can't be modified inside chage handler,
    *  as per https://bugs.openjdk.java.net/browse/JDK-8081700
    */
    private void filteredAlumnosListeners() {
        comboStudents.getEditor().textProperty().addListener((a, b, c) -> {
            Platform.runLater(() -> {
                filteredAlumnos.setPredicate(item -> item.toUpperCase().startsWith(c.toUpperCase()));
            });
        });
    }
    
//    public void setTimeLabel(TimeSlot start, TimeSlot end) {
//        timeLabel.setText(start.getStart().format(timeFormatter) + " - " + end.getEnd().format(timeFormatter));
//    }
//    
//    public void startVariables(TimeSlot start, TimeSlot end) {
//        setTimeLabel(start,end);
//        
//        tutoria.setFecha(start.getDate());
//        tutoria.setEstado(Tutoria.EstadoTutoria.PEDIDA);
//        tutoria.setInicio(start.getStart().toLocalTime());
//        tutoria.setDuracion(Duration.between(start.getStart(), end.getEnd()));
//        tutoria.anotacionesProperty().bind(boxDescription.textProperty());
//
//    }
    public void setTimeLabel() {
        timeLabel.setText(tutoria.getInicio().format(timeFormatter) + " - " + tutoria.getInicio().plus(tutoria.getDuracion()).format(timeFormatter));
    }
    
    public void startVariables(Tutoria tut) {
        tutoria = tut;
        setTimeLabel();
        subjectLabel.setText(tutoria.getAsignatura().toString());
        boxDescription.setText(tutoria.getAnotaciones());
        
        tutoria.anotacionesProperty().bind(boxDescription.textProperty());
    }
} 
    
    




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import application.SimpleAlumnoCell;
import application.SimpleSubjectCell;
import application.AlumnoCell;
import application.TimeSlot;
import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
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
    
    ObservableList<Alumno> listaAlumnos = AccesoBD.getInstance().getTutorias().getAlumnosTutorizados();
    ObservableList<Asignatura> listaAsignaturas = AccesoBD.getInstance().getTutorias().getAsignaturas();
    FilteredList<Alumno> filteredItems = new FilteredList<Alumno>(listaAlumnos, p -> true);
    
    @FXML
    private Button addButton;
    @FXML
    private ComboBox<Alumno> comboStudents;
    
    private Tutoria tutoria = new Tutoria();
    @FXML
    private Label errorLabel;
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
        listLV.setCellFactory(cel -> new SimpleAlumnoCell());
        
    
        comboStudents.setCellFactory(cel -> new SimpleAlumnoCell()); 
        comboSubject.setCellFactory(cel -> new SimpleSubjectCell());
        comboSubject.setButtonCell(new SimpleSubjectCell());
        comboSubject.setItems(listaAsignaturas);
        formatoComboStudents();

        
        comboStudents.getEditor().textProperty().addListener((a,b,c) -> {
            System.out.println(c);
//            if(!comboStudents.getSelectionModel().isEmpty() && (c.length() - b.length() == 1)) {
//                comboStudents.getSelectionModel().clearSelection();
//                comboStudents.setValue(null);
//            }

            if(!c.isEmpty()) {
                comboStudents.show();
                Alumno alumno = checkMemberOf(c);
                if(alumno != null) {
                    comboStudents.setValue(alumno);
                }
            }
        
        });
        
        

        
    }
    
    private void cancelar(ActionEvent event) {
        ((Stage) boxDescription.getScene().getWindow()).close();
    }

    @FXML
    private void addStudent(ActionEvent event) {
        System.out.println(comboStudents.getEditor().getText());
        if ( datos.contains(comboStudents.getValue()) ) {
            errorLabel.setText("No se pueden añadir alumnos repetidos.");
            comboStudents.getEditor().setText("");
            comboStudents.getSelectionModel().clearSelection();
            comboStudents.setValue(null);
            comboStudents.requestFocus();
        }
        
        else {
            Alumno alumnoBuscado = checkMemberOf(comboStudents.getEditor().getText());
            if (alumnoBuscado != null) {
                datos.add(alumnoBuscado);
                comboStudents.getEditor().setText("");
                comboStudents.getSelectionModel().clearSelection();
                comboStudents.setValue(null);
                comboStudents.requestFocus();
                listLV.refresh();
            } else if ((!comboStudents.getEditor().getText().isEmpty())
                    && (comboStudents.getEditor().getText().trim().length() != 0)) {
                datos.add(comboStudents.getValue());
                comboStudents.getEditor().setText("");
                comboStudents.getSelectionModel().clearSelection();
                comboStudents.setValue(null);
                comboStudents.requestFocus();
                listLV.refresh();
            }
        }
    }

    private Alumno checkMemberOf(String nombreAlumno){ //método que comprueba si texto del comboStudents es igual a algun alumno
        Alumno res = null;
        for(int i = 0; i <= filteredItems.size()&& res == null; i++){
            if((filteredItems.get(i).getNombre() + " " + filteredItems.get(i).getApellidos()).toUpperCase().equals(nombreAlumno.toUpperCase())) {
                res = filteredItems.get(i);
            }
                
        }
        return res;
    }
    
    @FXML
    private void cancelMethod(ActionEvent event) {
        FXMLCalendarioController.setTutoria(null);
        ((Stage) boxDescription.getScene().getWindow()).close();
    }

    @FXML
    private void acceptMethod(ActionEvent event) {
        if(comboSubject.getValue() == null || datos.isEmpty()){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Alerta de guardado");
            alert.setHeaderText("Imposible guardar la tutoría");
            alert.setContentText("Por favor, compruebe que: \n\t· Ha elegido el tipo de asignatura. \n\t· Introducido al menos 1 alumno.");
            alert.showAndWait();
        }
        else{
            FXMLCalendarioController.setTutoria(tutoria);
            ((Stage) boxDescription.getScene().getWindow()).close();
        }
    }
    
    
    
    //METODO COPIADO DE STACK OVERFLOW https://stackoverflow.com/questions/19010619/javafx-filtered-combobox https://stackoverflow.com/questions/46988860/java-fx-editable-combobox-with-objects
    private void formatoComboStudents() {
        comboStudents.setConverter(new StringConverter<Alumno>() {
        @Override
        public String toString(Alumno alumno) {
            return alumno == null ? "" : alumno.getNombre() + " " + alumno.getApellidos();
        }

            @Override
            public Alumno fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        
        comboStudents.getEditor().textProperty().addListener((obs, oldValue, newValue) -> { //no funciona del todo
            final TextField editor = comboStudents.getEditor();
            final Alumno selected = comboStudents.getSelectionModel().getSelectedItem();
            
            Platform.runLater(() -> {
                if (selected == null || !selected.equals(editor.getText())) {
                    filteredItems.setPredicate(item -> {
                        if (((item.getNombre() + " " + item.getApellidos()).toUpperCase()).startsWith(newValue.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
        });
        comboStudents.setItems(filteredItems);
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




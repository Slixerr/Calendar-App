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
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
        listLV.setCellFactory(cel -> new AlumnoList());
        
        comboStudents.setCellFactory(cel -> new AlumnoList()); 
        comboSubject.setCellFactory(cel -> new SubjectList());
        comboSubject.setButtonCell(new SubjectList());
        comboSubject.setItems(listaAsignaturas);
        formatoComboStudents();

        
        comboStudents.getEditor().textProperty().addListener((a,b,c) -> {
            if(!comboStudents.getEditor().getText().isEmpty()) {
                comboStudents.show();
            }
        });
        
        

        
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
            comboStudents.getSelectionModel().clearSelection();
            comboStudents.setValue(null);
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
        
        FilteredList<Alumno> filteredItems = new FilteredList<Alumno>(listaAlumnos, p -> true);
        comboStudents.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = comboStudents.getEditor();
            final Alumno selected = comboStudents.getSelectionModel().getSelectedItem();
            
            Platform.runLater(() -> {
                if (selected == null || !selected.equals(editor.getText())) {
                    filteredItems.setPredicate(item -> {
                        if (((item.getNombre() + " " + item.getApellidos()).toUpperCase()).startsWith(newValue.toUpperCase())) {
                            System.out.println("true");
                            return true;
                        } else {
                            System.out.println("false");
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

class AlumnoList extends ListCell<Alumno>{

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

    
class SubjectList extends ListCell<Asignatura>{

    @Override
    protected void updateItem(Asignatura item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if(empty || item == null) {
            setText("");
        }
        else {
            setText(item.getDescripcion());
        }
    }
}


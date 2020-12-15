/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import referencias.modelo.Alumno;

/**
 * FXML Controller class
 *
 * @author danie
 */
public class FXMLAlumnoController implements Initializable {

    @FXML
    private Button photoButton;
    @FXML
    private TextField nameBox;
    @FXML
    private TextField surnameBox;
    @FXML
    private TextField mailBox;
    @FXML
    private Button cancelButton;
    @FXML
    private Button createButton;
    
    private Alumno alumno;
    
    private Stage stage;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        alumno = new Alumno();
        nameBox.textProperty().bind(alumno.nombreProperty());
        surnameBox.textProperty().bind(alumno.apellidosProperty());
        mailBox.textProperty().bind(alumno.emailProperty());
        createButton.setOnAction(a -> {
            FXMLCalendarioController.setCreatedAlumno(alumno);
            stage.close();
        });
        cancelButton.setOnAction(a -> {
            FXMLCalendarioController.setCreatedAlumno(null);
            stage.close();
        });
        String property = System.getProperty("user.home");
        photoButton.setOnAction(a -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Elegir foto");
            fc.setInitialDirectory(new File(property));
            File file = fc.showOpenDialog(stage);
            if (file != null) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (Exception ignore) {}
            }
        });
    }
    
    public void setName(String name) {
        alumno.setNombre(name);
    }
    
    public void setSurname(String surname) {
        alumno.setApellidos(surname);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

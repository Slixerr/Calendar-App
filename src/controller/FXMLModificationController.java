/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author silvi
 */
public class FXMLModificationController implements Initializable {

    @FXML
    private Label timeLabel;
    @FXML
    private ComboBox<?> comboSubject;
    @FXML
    private ListView<?> listLV;
    @FXML
    private ComboBox<?> comboStudents;
    @FXML
    private Button addButton;
    @FXML
    private TextArea boxDescription;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addStudent(ActionEvent event) {
    }

    @FXML
    private void cancelMethod(ActionEvent event) {
    }

    @FXML
    private void acceptMethod(ActionEvent event) {
    }
    
}

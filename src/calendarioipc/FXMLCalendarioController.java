/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioipc;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 *
 * @author silvi
 */
public class FXMLCalendarioController implements Initializable {
    
    private Label label;
    @FXML
    private DatePicker day;
    @FXML
    private GridPane grid;
    @FXML
    private Label lunesCol;
    @FXML
    private Label martesCol;
    @FXML
    private Label miercoles;
    @FXML
    private Label juevesCol;
    @FXML
    private Label viernesCol;
    @FXML
    private Label slotSelected;
    
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

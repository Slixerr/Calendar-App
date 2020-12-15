package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import referencias.modelo.Asignatura;


public class FXMLAsignaturaController implements Initializable {

    @FXML
    private TextField codigoBox;
    @FXML
    private TextField nombreBox;
    @FXML
    private Button cancelButton;
    @FXML
    private Button createButton;
    
    private Asignatura asignatura;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        asignatura = new Asignatura();
        asignatura.codigoProperty().bind(codigoBox.textProperty());
        asignatura.descripcionProperty().bind(nombreBox.textProperty());
        createButton.setOnAction(a -> {
            FXMLCalendarioController.setCreatedAsignatura(asignatura);
            ((Stage) createButton.getScene().getWindow()).close();
        });
        cancelButton.setOnAction(a -> {
            FXMLCalendarioController.setCreatedAsignatura(null);
            ((Stage) cancelButton.getScene().getWindow()).close();
        });
        
    }    
    
}

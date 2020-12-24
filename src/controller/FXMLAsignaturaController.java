package controller;

import static controller.FXMLAlumnoController.MODIFICAR;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import referencias.accesoBD.AccesoBD;
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
    
    @FXML
    private Label title;
    @FXML
    private Label errorLabel;
    
    private final StringProperty codigo = new SimpleStringProperty();
    private final StringProperty descripcion = new SimpleStringProperty();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codigo.bind(codigoBox.textProperty());
        descripcion.bind(nombreBox.textProperty());
        
        createButton.setOnAction(a -> {
            if (codigoBox.getText().isEmpty() || nombreBox.getText().isEmpty()) {
                errorLabel.setText("Por favor, rellene todos los campos");
            } else {
                asignatura.setCodigo(codigo.getValue());
                asignatura.setDescripcion(descripcion.getValue());
                ((Stage) createButton.getScene().getWindow()).close();
            }
        });
        cancelButton.setOnAction(a -> {
            ((Stage) cancelButton.getScene().getWindow()).close();
        });
    }    

    void setAsignatura(Asignatura as) {
        nombreBox.setText(as.getDescripcion());
        codigoBox.setText(as.getCodigo());
        asignatura = as;
    }

    void setType(int type) {
        if(type == MODIFICAR) {
            createButton.setText("Modificar");
            title.setText("Modificar asignatura");
        }
        else {
            createButton.setText("Crear");
            title.setText("Añadir asignatura");
        }
    }
}

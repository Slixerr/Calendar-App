package controller;

import static controller.FXMLAlumnoController.MODIFICAR;
import java.net.URL;
import java.util.ResourceBundle;
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
    
    private int color;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        asignatura = new Asignatura();
        asignatura.codigoProperty().bind(codigoBox.textProperty());
        asignatura.descripcionProperty().bind(nombreBox.textProperty());
        createButton.setOnAction(a -> {
            if (codigoBox.getText().isEmpty() && nombreBox.getText().isEmpty()) {
                errorLabel.setText("Por favor, rellene todos los campos");
            } else {
                FXMLCalendarioController.setCreatedAsignatura(asignatura);
                ((Stage) createButton.getScene().getWindow()).close();
            }
        });
        cancelButton.setOnAction(a -> {
            FXMLCalendarioController.setCreatedAsignatura(null);
            ((Stage) cancelButton.getScene().getWindow()).close();
        });
        setColor();
        
    }    

    void setDescription(String description) {
        nombreBox.setText(description);
    }

    void setCode(String code) {
        codigoBox.setText(code);
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
    
    void setColor() {
        int nAs = AccesoBD.getInstance().getTutorias().getAsignaturas().size()+1;
        int logVal = (int)(Math.floor(Math.log(nAs)/Math.log(2)));
        color = (int) ((255/(Math.pow(2,logVal+1))) * ((nAs-Math.pow(2, logVal)+1)*2 - 1));
        System.out.println(color);
    }
    
}

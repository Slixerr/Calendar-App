package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import referencias.modelo.Alumno;


public class FXMLAlumnoController implements Initializable {
    
    public static final int CREAR = 0, MODIFICAR = 1;

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
    @FXML
    private ImageView headshotView;
    @FXML
    private Label title;
    @FXML
    private Label errorLabel;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        alumno = new Alumno();
        alumno.nombreProperty().bind(nameBox.textProperty());
        alumno.apellidosProperty().bind(surnameBox.textProperty());
        alumno.emailProperty().bind(mailBox.textProperty());
        
        
        Image img = new Image(FXMLAlumnoController.class.getResourceAsStream("/resources/headshot.png"));

        headshotView.imageProperty().bind(
                Bindings.when(alumno.headshotProperty().isNull())
                        .then(img)
                        .otherwise(alumno.headshotProperty()));

        createButton.setOnAction(a -> {
            if (nameBox.getText().isEmpty() || surnameBox.getText().isEmpty() || mailBox.getText().isEmpty()) {
                errorLabel.setText("Por favor, rellene todos los campos");
            } else {
                FXMLCalendarioController.setCreatedAlumno(alumno);
                stage.close();
            }
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
                    alumno.setHeadShot(new Image(file.toURI().toString()));
                } catch (Exception ignore) {}
            }
        });
    }
    
    public void setName(String name) {
        nameBox.setText(name);
    }
    
    public void setSurname(String surname) {
        surnameBox.setText(surname);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setEmail(String sSurname) {
        mailBox.setText(sSurname);
    }

    void setHeadshot(Image hs) {
        alumno.setHeadShot(hs);
    }
    
    public void setType(int type) {
        if(type == MODIFICAR) {
            createButton.setText("Modificar");
            title.setText("Modificar alumno");
        }
        else {
            createButton.setText("Crear");
            title.setText("Añadir alumno");
        }
    }
}

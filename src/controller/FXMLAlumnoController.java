package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
    
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty apellidos = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final ObjectProperty<Image> headshot = new SimpleObjectProperty<>();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nombre.bind(nameBox.textProperty());
        apellidos.bind(surnameBox.textProperty());
        email.bind(mailBox.textProperty());
        
        Image img = new Image(FXMLAlumnoController.class.getResourceAsStream("/resources/headshot.png"));

        headshotView.imageProperty().bind(
                Bindings.when(headshot.isNull())
                        .then(img)
                        .otherwise(headshot));

        createButton.setOnAction(a -> {
            if (nameBox.getText().isEmpty() || surnameBox.getText().isEmpty() || mailBox.getText().isEmpty()) {
                errorLabel.setText("Por favor, rellene todos los campos");
            } else {
                FXMLCalendarioController.setCreatedAlumno(alumno);
                alumno.setNombre(nombre.getValue());
                alumno.setApellidos(apellidos.getValue());
                alumno.setEmail(email.getValue());
                alumno.setHeadShot(headshot.getValue());
                ((Stage) createButton.getScene().getWindow()).close();
            }
        });
        
        cancelButton.setOnAction(a -> {
            FXMLCalendarioController.setCreatedAlumno(null);
            ((Stage) cancelButton.getScene().getWindow()).close();
        });
        
        String property = System.getProperty("user.home");
        photoButton.setOnAction(a -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Elegir foto");
            fc.setInitialDirectory(new File(property));
            File file = fc.showOpenDialog(stage);
            if (file != null) {
                try {
                    headshot.set(new Image(file.toURI().toString()));
                } catch (Exception ignore) {}
            }
        });
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

    void setAlumno(Alumno al) {
        nameBox.setText(al.getNombre());
        surnameBox.setText(al.getApellidos());
        mailBox.setText(al.getEmail());
        headshot.set(al.getHeadShot());
        alumno = al;
    }
}

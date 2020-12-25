/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import controller.FXMLCalendarioController;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import referencias.accesoBD.AccesoBD;

/**
 *
 * @author silvi
 */
public class CalendarioIPC extends Application {

    public static final LocalTime SLOTS_LAST = LocalTime.of(19, 50);
    public static final int ROW_SPAN = 87; // for this aproach to work ALL ROWS AND COLUMNS MUST BE THE SAME SIZE
    public static final LocalTime SLOTS_FIRST = LocalTime.of(8, 0);
    
    public static final Duration SLOT_LENGTH = Duration.ofMinutes(10);
    public static final int COL_SPAN = 5;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Locale locale = new Locale("es", "ES");//Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle("resources.labelText",locale);
        Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLCalendario.fxml"), bundle);
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setMinHeight(700);
        stage.setMinWidth(900);
        stage.show();
        
        FXMLCalendarioController.setScene(scene);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        AccesoBD.getInstance().salvar();
    }
    
}

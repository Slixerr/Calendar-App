
package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import referencias.modelo.Tutoria;
import referencias.modelo.Tutorias;


public class CalendarioChildBuilder {
    
    private DatePicker dayPicker;
    
    private Label subjectLabel;
    
    private Label studentsLabel;
    
    private Label descriptionLabel;
    
    private AnchorPane descriptionBox;
    
    private GridPane timeTable;
    
    private Label lunesCol;
    
    private Label martesCol;
    
    private Label miercoles;
    
    private Label juevesCol;
    
    private Label viernesCol;
    
    public static int pressedCol = 0;
    private TimeSlot lastHovered = null;
    private Label slotSelected;
    
    private static Tutoria createdTut;
    
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

    // se puede cambiar por codigo la pseudoclase activa de un nodo    
    public static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    public static final PseudoClass BOOKED_PSEUDO_CLASS = PseudoClass.getPseudoClass("active");

    private List<List<TimeSlot>> timeSlots = new ArrayList<>(); //Para varias columnas List<List<TimeSlot>>

    private ObjectProperty<LocalDateTime[]> bookingTime;//refactor to class

    private List<Label> diasSemana;
    
    private Bounds gridBounds;
    
    private Tutorias tutorias;
    
    private List<Tutoria> weekTutorias;
    
    private final BooleanProperty descriptionShowing = new SimpleBooleanProperty();
    
    private Node baseView = null;
    
    private static Scene scene;
}

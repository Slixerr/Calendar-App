/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import controller.FXMLCalendarioController;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import referencias.modelo.Tutoria;

/**
 *
 * @author danie
 */
public class TimeSlot extends Position{
    
    private final LocalDateTime start;
    protected final Pane view;
    private boolean selectedBlocked = false;
    private final BooleanProperty selected = new SimpleBooleanProperty();
    private final BooleanProperty booked = new SimpleBooleanProperty();
    private final ObjectProperty<Tutoria> tutoriaProperty = new SimpleObjectProperty<>();
    
    public final BooleanProperty selectedProperty() {
        return selected;
    }
    
    public Tutoria getTutoria() {
        return tutoriaProperty.getValue();
    }
    
    public final void blockSelected() {
        selectedBlocked = true;
    }
    
    public final void unBlockSelected() {
        selectedBlocked = false;
    }
    
    public final BooleanProperty bookedProperty() {
        return booked;
    }

    public final boolean isSelected() {
        return selectedProperty().get();
    }
    
    public final boolean isBooked() {
        return bookedProperty().get();
        //return tutoriaProperty().get() != null
    }

    public final void setSelected(boolean selected) {
        if(!selectedBlocked) selectedProperty().set(selected);
    }

    public TimeSlot(LocalDateTime start, Position gridPos, List<Tutoria> tutorias) {
        super(gridPos.col, gridPos.row);
        this.start = start;
        view = new Pane();
        view.getStyleClass().add("time-slot");
        bookedProperty().set(false);
        selectedProperty().addListener((obs, wasSelected, isSelected) -> view.pseudoClassStateChanged(FXMLCalendarioController.SELECTED_PSEUDO_CLASS, isSelected));
        bookedProperty().addListener((obs, wasBooked, isBooked) -> view.pseudoClassStateChanged(FXMLCalendarioController.BOOKED_PSEUDO_CLASS, isBooked));
    
        instatiateTutoriaProperty(tutorias);
        bookedProperty().bind(Bindings.notEqual(tutoriaProperty,Tutoria.nullValue()));
    }
    
    public void instatiateTutoriaProperty(List<Tutoria> tutorias) {
        tutoriaProperty.set(tutorias.stream().filter((Tutoria tutoria) -> {
            LocalDate fecha = tutoria.getFecha();
            LocalTime hora = tutoria.getInicio();
            Duration duracion = tutoria.getDuracion();
            return start.toLocalDate().equals(fecha) && 
                    start.toLocalTime().compareTo(hora) >= 0 && 
                    start.toLocalTime().compareTo(hora.plus(duracion)) < 0;
        }).findAny().orElse(null));
    }

    @Deprecated
    public void setBooked() {
        /*ObservableList<String> styles = view.getStyleClass();
        styles.remove("time-slot");
        styles.add("time-slot-libre");*/
        bookedProperty().set(true);
    }
    
    public void setTutoria(Tutoria tutoria) {
        tutoriaProperty.set(tutoria);
    }

    public LocalDateTime getStart() {
        return start;
    }
    
    public LocalDateTime getEnd() {
        return start.plus(CalendarioIPC.SLOT_LENGTH);
    }
    
    public static int rowFromTime(LocalDateTime time) {
        return (time.getHour()-8)*6 + (int)(time.getMinute()/10);
    }

    public LocalTime getTime() {
        return start.toLocalTime();
    }

    public LocalDate getDate() {
        return start.toLocalDate();
    }

    public DayOfWeek getDayOfWeek() {
        return start.getDayOfWeek();
    }

    public Node getView() {
        return view;
    }
    
}

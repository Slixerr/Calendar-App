/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import static application.CalendarioIPC.SLOT_LENGTH;
import controller.FXMLCalendarioController;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import referencias.modelo.Tutoria;

public class TimeSlot extends Position{
    public static final int EMPTY=0,TOP=1,MIDDLE=2,BOTTOM=3;
    private final LocalDateTime start;
    protected final Pane view;
    private boolean selectedBlocked = false;
    private final IntegerProperty stateProperty = new SimpleIntegerProperty();//should be enum I think
    private final BooleanProperty selectedProperty = new SimpleBooleanProperty(); 
    private final ObjectProperty<Tutoria> tutoriaProperty = new SimpleObjectProperty<>();
    
    public final BooleanProperty selectedProperty() {
        return selectedProperty;
    }
    
    public void setState(int n) {
        stateProperty.set(n);
    }
    
    public Tutoria getTutoria() {
        return tutoriaProperty.getValue();
    }
    
    public void isTopLine() {
        view.pseudoClassStateChanged(FXMLCalendarioController.MID_PSEUDO_CLASS, true);
    }
    
    public int compareRows(TimeSlot other) {
        return this.getRow()-other.getRow();
    }
    
    public final void blockSelected() {
        selectedBlocked = true;
    }
    
    public final void unBlockSelected() {
        selectedBlocked = false;
    }

    public final boolean isSelected() {
        return selectedProperty.getValue();
    }
    
    public final boolean isBooked() {
        return getTutoria() != null;
    }

    public final void setSelected(boolean selected) {
        if(!selectedBlocked) selectedProperty().set(selected);
    }

    public TimeSlot(LocalDateTime start, Position gridPos, List<Tutoria> tutorias) {
        super(gridPos.col, gridPos.row);
        this.start = start;
        view = new Pane();
        view.getStyleClass().add("time-slot");
        selectedProperty.addListener((a, b, isSelected) -> {
            view.pseudoClassStateChanged(FXMLCalendarioController.SELECTED_PSEUDO_CLASS, isSelected);
        });
        tutoriaProperty.addListener((a, b, tut) -> {
            view.pseudoClassStateChanged(FXMLCalendarioController.BOOKED_PSEUDO_CLASS, tut != null);
            Color col = Color.hsb(tutoriaProperty.getValue().getAsignatura().getColor()*360/255.0, 60/100.0, 80/100.0);
            System.out.println(tutoriaProperty.getValue().getAsignatura().getColor());
            view.setStyle("-fx-background-color: "+toHexString(col)+";");
        });
        stateProperty.set(EMPTY);
        stateProperty.addListener((a, b, state) -> {
            view.pseudoClassStateChanged(FXMLCalendarioController.TOP_PSEUDO_CLASS, state.intValue() == TOP);
            view.pseudoClassStateChanged(FXMLCalendarioController.MIDDLE_PSEUDO_CLASS, state.intValue() == MIDDLE);
            view.pseudoClassStateChanged(FXMLCalendarioController.BOTTOM_PSEUDO_CLASS, state.intValue() == BOTTOM);
        });
        
        instatiateTutoriaProperty(tutorias);
    }
    
    // código copiado de respuesta de usuario Kröw a https://stackoverflow.com/questions/17925318/how-to-get-hex-web-string-from-javafx-colorpicker-color
    private String format(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }
    public String toHexString(Color value) {
        return "#" + (format(value.getRed()) + format(value.getGreen()) + format(value.getBlue()) + format(value.getOpacity()))
                .toUpperCase();
    }
    
    public final void instatiateTutoriaProperty(List<Tutoria> tutorias) {
        tutoriaProperty.set(tutorias.stream().filter((Tutoria tutoria) -> {
            LocalDate fecha = tutoria.getFecha();
            LocalTime hora = tutoria.getInicio();
            Duration duracion = tutoria.getDuracion();
            return start.toLocalDate().equals(fecha) && 
                    start.toLocalTime().compareTo(hora) >= 0 && 
                    start.toLocalTime().compareTo(hora.plus(duracion)) < 0;
        }).findAny().orElse(null));
        Tutoria tut = tutoriaProperty.getValue();
        if (tut != null) {
            if (tut.getInicio().equals(this.getStart().toLocalTime())) {
                setState(TOP);
                if (tut.getDuracion().equals(SLOT_LENGTH)) {
                    setState(MIDDLE);
                }
            } else if (tut.getInicio().plus(tut.getDuracion()).equals(this.getEnd().toLocalTime())) {
                setState(BOTTOM);
            }
        }
    }
    
    public boolean inHour(TimeSlot t) {
        return (int)(Math.abs(Duration.between(this.getStart(), t.getStart()).toMinutes())) < 60;
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

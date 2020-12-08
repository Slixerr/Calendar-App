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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;


/**
 *
 * @author danie
 */
public class TimeSlot extends Position{
    
    private final LocalDateTime start;
    private final Duration duration;
    protected final Pane view;
    private boolean booked;
    private final BooleanProperty selected = new SimpleBooleanProperty();

    public final BooleanProperty selectedProperty() {
        return selected;
    }

    public final boolean isSelected() {
        return selectedProperty().get();
    }

    public final void setSelected(boolean selected) {
        selectedProperty().set(selected);
    }

    public TimeSlot(LocalDateTime start, Duration duration, Position gridPos, TimeSlot last, boolean booked) {
        super(gridPos.col, gridPos.row);
        this.start = start;
        this.duration = duration;
        view = new Pane();
        view.getStyleClass().add("time-slot");
        // ---------------------------------------------------------------
        // de esta manera cambiamos la apariencia del TimeSlot cuando los seleccionamos
        selectedProperty().addListener((obs, wasSelected, isSelected) -> view.pseudoClassStateChanged(FXMLCalendarioController.SELECTED_PSEUDO_CLASS, isSelected));
        this.booked = booked;
    }

    public void setBooked() {
        ObservableList<String> styles = view.getStyleClass();
        styles.remove("time-slot");
        styles.add("time-slot-libre");
        booked = true;
    }

    public LocalDateTime getStart() {
        return start;
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

    public Duration getDuration() {
        return duration;
    }

    public Node getView() {
        return view;
    }

    public boolean isBooked() {
        return booked;
    }
    
}

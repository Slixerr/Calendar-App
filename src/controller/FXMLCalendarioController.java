/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import application.CalendarioIPC;
import static application.CalendarioIPC.SLOTS_FIRST;
import static application.CalendarioIPC.SLOTS_LAST;
import static application.CalendarioIPC.SLOT_LENGTH;
import application.Position;
import application.TimeSlot;
import application.Week;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import referencias.accesoBD.AccesoBD;
import referencias.modelo.Alumno;
import referencias.modelo.Tutoria;
import referencias.modelo.Tutorias;

public class FXMLCalendarioController implements Initializable {

    @FXML
    private DatePicker dayPicker;
    
    @FXML
    private Label subjectLabel;
    @FXML
    private Label studentsLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private AnchorPane descriptionBox;
    @FXML
    private GridPane timeTable;
    @FXML
    private Label lunesCol;
    @FXML
    private Label martesCol;
    @FXML
    private Label miercoles;
    @FXML
    private Label juevesCol;
    @FXML
    private Label viernesCol;
    

    @FXML
    private void alumnoMethod(ActionEvent event) {
    }

    @FXML
    private void subjectsMethod(ActionEvent event) {
    }

    @FXML
    private void canceledMethod(ActionEvent event) {
    }

    @FXML
    private void editMethod(ActionEvent event) {
    }

    @FXML
    private void closeMethod(ActionEvent event) {
    }
    
    public static final int TIME_COL_WIDTH = 100; // Since the first column doesnt rezise, a special case is implemented for it
    
    public static int pressedCol = 0;
    private TimeSlot lastHovered = null;
    private Label slotSelected;
    
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accessTutorias();
        addDayLabels();
        createBoundsListener();
        createBookingListener();
        createDayListener();
        createDescriptionListener();
    }
    
    public void accessTutorias() {
        tutorias = AccesoBD.getInstance().getTutorias();
    }
    
    private void addDayLabels() {
        diasSemana= new ArrayList<>();
        diasSemana.add(lunesCol);
        diasSemana.add(martesCol);
        diasSemana.add( miercoles);
        diasSemana.add(juevesCol);
        diasSemana.add( viernesCol);
    }
    
    private void createBoundsListener() {
        //gridBounds = timeTable.localToScene(timeTable.getBoundsInLocal());
        timeTable.boundsInLocalProperty().addListener((a,b,c) -> {  //use 2 listeners
            gridBounds = timeTable.localToScene(c);
        });
        timeTable.localToSceneTransformProperty().addListener((a,b,c) -> {  //use 2 listeners
            gridBounds = timeTable.localToScene(timeTable.getBoundsInLocal());
        });
    }

    private void createBookingListener() {
        bookingTime = new SimpleObjectProperty<>();
        bookingTime.addListener((a, b, c) -> {
            if (c!= null) {
                reorder(c);
                modifyTimeLabel(c[0], c[1]);
                try{
                    timeTable.add(slotSelected, pressedCol, TimeSlot.rowFromTime(c[0]));}
                catch(Exception ignored){}
            }
        });
    }
    
    private void createDescriptionListener() {
        descriptionShowing.set(false);
        descriptionBox.disableProperty().bind(Bindings.not(descriptionShowing));
        descriptionBox.visibleProperty().bind(descriptionShowing);
    }
    
    private void bindDescriptionTo(Tutoria tut) {
        descriptionLabel.textProperty().bind(tut.anotacionesProperty());
        subjectLabel.textProperty().bind(tut.asignaturaProperty());
        studentsLabel.textProperty().set(tut.getAlumnos().stream().map(Alumno::toString).collect(Collectors.joining("\n")));
    }
    
    private void reorder(LocalDateTime t[]) {
        if (t[1].isBefore(t[0])){
            LocalDateTime temp = t[0];
            t[0] = t[1];
            t[1] = temp;
        }
    }
    
    private void modifyTimeLabel(LocalDateTime t1, LocalDateTime t2) {
        if (t1 == null) {
            slotSelected.setText("");
        } else {
            slotSelected.setText(t1.format(timeFormatter));
        }
        if (t2 == null) {
            slotSelected.setText(slotSelected.getText()+" - ");
        } else {
            slotSelected.setText(slotSelected.getText()+" - "+t2.format(timeFormatter));
        }
    }

    private void createDayListener() {
        dayPicker.setValue(LocalDate.MIN);
        dayPicker.valueProperty().addListener((a, b, c) -> {
            clearTimeTable();
            
            Week week = new Week(c);
        
            findTutorias(week);
            
            updateTimeTable(week);
            
            addLabels(week);
        });
        dayPicker.setValue(LocalDate.now());
    }

    private void findTutorias(Week now) {
        weekTutorias = tutorias.getTutoriasConcertadas().stream().filter((Tutoria tutoria) -> {
            LocalDate fecha = tutoria.getFecha();
            return now.getStartOfWeek().isBefore(fecha) && now.getEndOfWeek().isAfter(fecha) ||
                    now.getStartOfWeek().isEqual(fecha) || now.getEndOfWeek().isEqual(fecha);
        }).collect(Collectors.toList());
    }

    private void updateTimeTable(Week now) {
        int dayIndex = 1;
        for (LocalDate day = now.getStartOfWeek(); 
                !day.isAfter(now.getEndOfWeek()); 
                day = day.plusDays(1), dayIndex++) {
            
            diasSemana.get(dayIndex - 1).setText(day.getDayOfWeek()+System.lineSeparator()+day.toString());
            fillDaySlots(day, dayIndex);
        }
    }
    
    private void addLabels(Week now) {
        weekTutorias.forEach(tutoria -> {
            Label l = new Label();
            LocalDateTime start = LocalDateTime.of(tutoria.getFecha(),tutoria.getInicio());
            LocalDateTime end = start.plus(tutoria.getDuracion());
            l.setText(start.format(timeFormatter)+" - "+end.format(timeFormatter));
            l.setMouseTransparent(true);
            
            int col = (int)(Period.between(now.getStartOfWeek(), tutoria.getFecha()).getDays())+1;
            int row = (int)(Duration.between(SLOTS_FIRST, tutoria.getInicio()).toMinutes()/SLOT_LENGTH.toMinutes());
            timeTable.add(l,col,row);
        });
    }

    private void fillDaySlots(LocalDate day, int dayIndex) {
        List<TimeSlot> daySlots = new ArrayList<>();
        timeSlots.add(daySlots);
       
        int slotIndex = 0;
        for (LocalDateTime startTime = day.atTime(SLOTS_FIRST);
                !startTime.isAfter(day.atTime(SLOTS_LAST));
                startTime = startTime.plus(SLOT_LENGTH), slotIndex++) {
            
            TimeSlot timeSlot = createTimeSlot(startTime, new Position(dayIndex, slotIndex));
            
            daySlots.add(timeSlot);
            timeTable.add(timeSlot.getView(), dayIndex, slotIndex);
        }
    }

    private TimeSlot createTimeSlot(LocalDateTime startTime, Position gridPos) {
        TimeSlot timeSlot = new TimeSlot(startTime, gridPos, weekTutorias);
        registerHandlers(timeSlot);
        return timeSlot;
    }

    private void clearTimeTable() {
        //clear booking selections
        bookingTime.setValue(null);
        
        clearViews();
        
        timeSlots = new ArrayList<>();
    }

    private void clearViews() {
        ObservableList<Node> children = timeTable.getChildren();
        timeSlots.forEach(dias -> {
            dias.forEach(timeSlot -> {
                children.remove(timeSlot.getView());
            });
        });
    }

    private void registerHandlers(TimeSlot timeSlot) {
        
        createOnMousePressedHandler(timeSlot);
        
        createOnMouseDraggedHandler(timeSlot);
        
        createOnMouseReleasedHandler(timeSlot);
    }

    private void createOnMousePressedHandler(TimeSlot timeSlot) {
        timeSlot.getView().setOnMousePressed((MouseEvent event) ->{
            if(slotSelected==null){
                slotSelected = new Label();
                slotSelected.setMouseTransparent(true);
            }
            resetTimeSlots();
            if(!timeSlot.isBooked()){
                descriptionShowing.set(false);
                
                pressedCol = (int)((event.getSceneX()-gridBounds.getMinX()- TIME_COL_WIDTH)/((timeTable.getWidth()-TIME_COL_WIDTH)/CalendarioIPC.COL_SPAN)) + 1;
                timeSlot.setSelected(true);
                
                timeSlot.blockSelected();
                lastHovered = timeSlot;

                bookingTime.setValue(new LocalDateTime[]{timeSlot.getStart(),timeSlot.getEnd()});
            }else{
                timeTable.getChildren().remove(slotSelected);
                slotSelected = null;
                
                bindDescriptionTo(timeSlot.getTutoria());
            }
        });
    }

    private void createOnMouseDraggedHandler(TimeSlot timeSlot) {
        timeSlot.getView().setOnMouseDragged((MouseEvent event) -> {
            if(lastHovered!=null){
                TimeSlot hovered = timeSlotOver(event);
                if (hovered != null && hovered != lastHovered && allowContinue(timeSlot, hovered)){
                    boolean movingDown = Math.abs(timeSlot.getRow() - hovered.getRow()) > 
                            Math.abs(timeSlot.getRow() - lastHovered.getRow());
                    if(movingDown) {
                        hovered.setSelected(true);
                    }else {
                        lastHovered.setSelected(false);
                    }
                    lastHovered = hovered;//This doesnt cover some weird cases created by fillBlanks
                    if (timeSlot.getRow() < hovered.getRow()) {
                        bookingTime.setValue(new LocalDateTime[]{timeSlot.getStart(),hovered.getEnd()});
                    }else {
                        bookingTime.setValue(new LocalDateTime[]{timeSlot.getEnd(),hovered.getStart()});
                    }
                }
            }
        });
    }

    private void createOnMouseReleasedHandler(TimeSlot timeSlot) {
        timeSlot.getView().setOnMouseReleased((MouseEvent event) -> {
            timeSlot.unBlockSelected();
            // confirmed on doubleClick
            if (lastHovered != null) {
                Tutoria result = createTutoria(timeSlot, lastHovered);
                if (result != null) {
                    boolean order = lastHovered.getRow() > timeSlot.getRow();
                    int max = order ? lastHovered.getRow() : timeSlot.getRow();
                    int min = !order ? lastHovered.getRow() : timeSlot.getRow();
                    for (int i = min; i <= max; i++) {// not sure
                        TimeSlot curr = timeSlots.get(pressedCol-1).get(i);
                        curr.setTutoria(result);
                        curr.setSelected(false);
                    }
                    slotSelected = new Label();
                    slotSelected.setMouseTransparent(true);
                    
                    weekTutorias.add(result);
                    tutorias.getTutoriasConcertadas().add(result);
                }
                resetTimeSlots();
                lastHovered = null;
            } else {
                descriptionShowing.set(true);
            }
        });
    }
        
    private boolean allowContinue(TimeSlot base, TimeSlot currTS) {
        boolean res = Math.abs(currTS.getRow() - lastHovered.getRow()) < 2;
        if(res) return true;
        res = fillBlanks(base, currTS);
        return res;
    }
    
    /*
    This method aims to reduce errors caused by moving the mouse too fast
    If it detects a space between the last cell the mouse has hovered over
    and the current cell, it will fill in the space (checking to see if there
    are any booked cells within.
    */
    private boolean fillBlanks(TimeSlot base, TimeSlot currentSlot) {
        
        boolean res = true;
        int i;
        int sign = (int)Math.signum(currentSlot.getRow()-lastHovered.getRow());
        
        TimeSlot currTS;
        for (i = lastHovered.getRow() + sign; res && i != currentSlot.getRow(); i+=sign) {
            currTS = timeSlots.get(pressedCol-1).get(i);
            res = !currTS.isBooked();
            if(res) {
                currTS.setSelected(!currTS.isSelected());
            }
        }
        
        if((Math.abs(base.getRow() - currentSlot.getRow())  <
            Math.abs(base.getRow() - lastHovered.getRow())) || (
            Math.signum(base.getRow() - currentSlot.getRow()) !=
            Math.signum(base.getRow() - lastHovered.getRow()))) {
            
            lastHovered.setSelected(false);
        }
        
        if(!res) sign*=2;
        
        lastHovered=timeSlots.get(pressedCol-1).get(i-sign);
        return res;
    }
    
    public void resetTimeSlots() {
        timeSlots.forEach(currDay -> {
            currDay.forEach(slot -> {
                slot.setSelected(false);
            });
        });
    }

    private TimeSlot timeSlotOver(MouseEvent event) { //utilizar posición
        Pane pane = (Pane)(event.getSource());//unnecessary but theres a bug in grid.getHeight() I don't understand
        Bounds paneBounds = pane.localToScene(pane.getBoundsInLocal());
        int row = (int)((event.getSceneY()-gridBounds.getMinY())/(paneBounds.getHeight()));
        TimeSlot slot = timeSlots.get(pressedCol-1).get(row);
        return (slot.isBooked()) ? null : slot;
    }

    private Tutoria createTutoria(TimeSlot start, TimeSlot end) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLSubject.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ignored) {}
        
        FXMLSubjectController controller = loader.getController();
        
        controller.startVariables(start, end);

        Scene scene = new Scene(root);
        Stage ventana2= new Stage();
        ventana2.setTitle("Ventana MODAL (2)");
        ventana2.initModality(Modality.APPLICATION_MODAL);
        //ventana2.initStyle(StageStyle.UNDECORATED);
        ventana2.setScene(scene);
        ventana2.showAndWait();
        
        //Only for testing
        Tutoria tut = new Tutoria();
        tut.fechaProperty().set(start.getDate());
        tut.inicioProperty().set(start.getTime());
        tut.duracionProperty().set(Duration.between(start.getStart(), end.getEnd()));
        return tut;
    }
}

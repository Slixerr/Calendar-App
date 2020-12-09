/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import application.CalendarioIPC;
import application.Position;
import application.TimeSlot;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
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
import referencias.modelo.Tutoria;

public class FXMLCalendarioController implements Initializable {

    @FXML
    private DatePicker day;
    
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

    private ObjectProperty<LocalDateTime[]> bookingTime;

    private List<Label> diasSemana;
    
    private Bounds gridBounds;
    
    private ObservableList<Tutoria> tutorias = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addDayLabels();
        createBoundsListener();
        createBookingListener();
        createDayListener();
        createDataLists();
    }
    
    private void createDataLists() {
        ArrayList<Tutoria> misdatos = new ArrayList<Tutoria>();
        tutorias = FXCollections.observableArrayList(misdatos);
        tutorias.addListener((Change<? extends Tutoria> c) -> {
             while (c.next()) {
                if (c.wasUpdated()) {
                    int start = c.getFrom() ;
                    int end = c.getTo() ;
                    for (int i = start ; i < end ; i++) {
                        //c.getList().get(i).
                    }
                }
            }
        });
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
            reorder(c);
            modifyTimeLabel(c[0], c[1]);
            try{
                timeTable.add(slotSelected, pressedCol, TimeSlot.rowFromTime(c[0]));}
            catch(Exception ignored){}
        });
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
        //inicializa el DatePicker al dia actual
        day.setValue(LocalDate.now());
        // pinta los SlotTime en el grid
        setTimeSlotsGrid(day.getValue());
        //cambia los SlotTime al cambiar de dia
        day.valueProperty().addListener((a, b, c) -> {
            setTimeSlotsGrid(c);
        });
    }

    private void setTimeSlotsGrid(LocalDate date) {
        //actualizamos la seleccion
        bookingTime.setValue(null);
  
        //borramos los SlotTime del grid
        ObservableList<Node> children = timeTable.getChildren();
        timeSlots.forEach(dias -> {
            dias.forEach(timeSlot -> {
                children.remove(timeSlot.getView());
            });
        });

        timeSlots = new ArrayList<>();

        // recorremos para cada Columna y creamos para cada slot
        LocalDate startOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(4);
        int diaIndex = 1;
        for (LocalDate dia = startOfWeek; !dia.isAfter(endOfWeek); dia = dia.plusDays(1)) {
            diasSemana.get(diaIndex - 1).setText(dia.getDayOfWeek()+System.lineSeparator()+dia.toString());
            List<TimeSlot> slotsDia = new ArrayList<>();
            timeSlots.add(slotsDia);
            // desde la hora de inicio y hasta la hora de fin creamos slotTime segun la duracion
            int slotIndex = 0;
            for (LocalDateTime startTime = dia.atTime(CalendarioIPC.SLOTS_FIRST);
                    !startTime.isAfter(dia.atTime(CalendarioIPC.SLOTS_LAST));
                    startTime = startTime.plus(CalendarioIPC.SLOT_LENGTH)) {

                // here is where the complexities of accesing a db should be implemented in part
                TimeSlot timeSlot = new TimeSlot(startTime, CalendarioIPC.SLOT_LENGTH, new Position(diaIndex, slotIndex), null);
                slotsDia.add(timeSlot);
                registerHandlers(timeSlot);
                
                timeTable.add(timeSlot.getView(), diaIndex, slotIndex);
                slotIndex++;
            }
            diaIndex++;
        }

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
            pressedCol = (int)((event.getSceneX()-gridBounds.getMinX()- TIME_COL_WIDTH)/((timeTable.getWidth()-TIME_COL_WIDTH)/CalendarioIPC.COL_SPAN)) + 1;
            timeSlot.setSelected(true);
            timeSlot.blockSelected();
            lastHovered = timeSlot;

            bookingTime.setValue(new LocalDateTime[]{timeSlot.getStart(),timeSlot.getEnd()});
        });
    }

    private void createOnMouseDraggedHandler(TimeSlot timeSlot) {
        timeSlot.getView().setOnMouseDragged((MouseEvent event) -> {
            TimeSlot hovered = timeSlotOver(event);
            if (hovered != null) {
                if (hovered != lastHovered && allowContinue(timeSlot, hovered)){
                    boolean movingDown = Math.abs(timeSlot.getRow() - hovered.getRow()) > 
                            Math.abs(timeSlot.getRow() - lastHovered.getRow());
                    if(movingDown) {
                        hovered.setSelected(true);
                    }else {
                        lastHovered.setSelected(false);
                    }
                    lastHovered = hovered;
                    if (timeSlot.getRow() < hovered.getRow()) {//check for when diff == 1 edge case
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
            if (event.getClickCount() > 1) {
                Optional<ButtonType> result = confirmSelection(timeSlot);
                if (result == null || result.isPresent() && result.get() == ButtonType.OK) {
                    boolean order = lastHovered.getRow() > timeSlot.getRow();
                    int max = order ? lastHovered.getRow() : timeSlot.getRow();
                    int min = !order ? lastHovered.getRow() : timeSlot.getRow();
                    for (int i = min; i <= max; i++) {// not sure
                        timeSlots.get(pressedCol-1).get(i).setBooked();
                    }
                    slotSelected = new Label();
                    slotSelected.setMouseTransparent(true);
                }
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

    private Optional<ButtonType> confirmSelection(TimeSlot timeSlot) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLSubject.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ignored) {}
        Scene scene = new Scene(root);
        Stage ventana2= new Stage();
        ventana2.setTitle("Ventana MODAL (2)");
        ventana2.initModality(Modality.APPLICATION_MODAL);
        ventana2.initStyle(StageStyle.UNDECORATED);
        ventana2.setScene(scene);
        ventana2.showAndWait();
        return null;
    }
}


 class MiCelda extends ListCell<Tutoria>{

    @Override
    protected void updateItem(Tutoria item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if(empty || item == null) {
            setText("");
        }
        else {
            //setText(item.getNombre());
        }
    }
        
    
    }

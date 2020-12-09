/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import application.Position;
import application.TimeSlot;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author silvi
 */
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
    
    private final LocalTime firstSlotStart = LocalTime.of(8, 0);
    public static final Duration SLOT_LENGTH = Duration.ofMinutes(10);
    private final LocalTime lastSlotStart = LocalTime.of(19, 50);
    
    
    // for this aproach to work ALL ROWS AND COLUMNS MUST BE THE SAME SIZE
    public final static int ROW_SPAN = 87;//Note that the implementation on the final table will be different as the initial row will not exist
    public final static int COL_SPAN = 5;
    private int TIME_COL = 100;
    
    private int pressedCol = 0;
    private TimeSlot lastHovered = null;
    private Label slotSelected;
    
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

    // se puede cambiar por codigo la pseudoclase activa de un nodo    
    public static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    private List<List<TimeSlot>> timeSlots = new ArrayList<>(); //Para varias columnas List<List<TimeSolt>>

    private ObjectProperty<LocalDateTime[]> bookingTime;

    private List<Label> diasSemana;
    
    private Bounds gridBounds;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gridBounds = timeTable.localToScene(timeTable.getBoundsInLocal());
        /*timeTable.boundsInLocalProperty().addListener((a,b,c) -> {
            gridBounds = timeTable.localToScene(c);
        });*/
        
        slotSelected = new Label();
        slotSelected.setMouseTransparent(true);
        // dejo los label de las columnas en un list<> para usarlo en un bucle
        diasSemana= new ArrayList<>();
        diasSemana.add(lunesCol);
        diasSemana.add(martesCol);
        diasSemana.add( miercoles);
        diasSemana.add(juevesCol);
        diasSemana.add( viernesCol);

      
        bookingTime = new SimpleObjectProperty<>();

        //---------------------------------------------------------------------
        //inicializa el DatePicker al dia actual
        day.setValue(LocalDate.now());

        //---------------------------------------------------------------------
        // pinta los SlotTime en el grid
        setTimeSlotsGrid(day.getValue());

        //---------------------------------------------------------------------
        //cambia los SlotTime al cambiar de dia
        day.valueProperty().addListener((a, b, c) -> {
            setTimeSlotsGrid(c);
        });

        bookingTime.addListener((a, b, c) -> {
            reorder(c);
            modifyTimeLabel(c[0], c[1]);
            timeTable.add(slotSelected, pressedCol, TimeSlot.rowFromTime(c[0]));
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

    private void setTimeSlotsGrid(LocalDate date) {
        //actualizamos la seleccion
        bookingTime.setValue(null);

        //--------------------------------------------        
        //borramos los SlotTime del grid
        ObservableList<Node> children = timeTable.getChildren();
        for (List<TimeSlot> dias : timeSlots) {
            for (TimeSlot timeSlot : dias) {
                for (Node node : children) {

                }
                children.remove(timeSlot.getView());
            }
        }

        timeSlots = new ArrayList<>();

        //---------------------------------------------------------------------------
        // recorremos para cada Columna y creamos para cada slot

        LocalDate startOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(4);
        int diaIndex = 1;
        for (LocalDate dia = startOfWeek; !dia.isAfter(endOfWeek); dia = dia.plusDays(1)) {
            diasSemana.get(diaIndex - 1).setText(dia.getDayOfWeek()+System.lineSeparator()+dia.toString());
            List<TimeSlot> slotsDia = new ArrayList<>();
            timeSlots.add(slotsDia);
            //----------------------------------------------------------------------------------
            // desde la hora de inicio y hasta la hora de fin creamos slotTime segun la duracion
            int slotIndex = 0;
            for (LocalDateTime startTime = dia.atTime(firstSlotStart);
                    !startTime.isAfter(dia.atTime(lastSlotStart));
                    startTime = startTime.plus(SLOT_LENGTH)) {

                // here is where the complexities of accesing a db should be implemented in part
                TimeSlot timeSlot = new TimeSlot(startTime, SLOT_LENGTH, new Position(diaIndex, slotIndex), null, false);
                slotsDia.add(timeSlot);
                registerHandlers(timeSlot);
                
                timeTable.add(timeSlot.getView(), diaIndex, slotIndex);
                slotIndex++;
            }
            diaIndex++;
        }

    }

    private void registerHandlers(TimeSlot timeSlot) {
        timeSlot.getView().setOnMousePressed((MouseEvent event) -> {
            resetTimeSlots();
            gridBounds = timeTable.localToScene(timeTable.getBoundsInLocal());
            pressedCol = (int)((event.getSceneX()-gridBounds.getMinX()- TIME_COL)/((timeTable.getWidth()-TIME_COL)/COL_SPAN)) + 1;
            timeSlot.setSelected(true);
            lastHovered = timeSlot;
            
            bookingTime.setValue(new LocalDateTime[]{timeSlot.getStart(),timeSlot.getEnd()});

            //Pane pane = (Pane)(event.getSource());
            //Bounds pb = pane.localToScene(pane.getBoundsInLocal());
            //Bounds pb2 = timeTable.getBoundsInParent();
            //System.out.println(pb.getHeight()+"  "+pb2.getHeight()/ROW_SPAN);
            //System.out.println(pb.getWidth()+"  "+timeTable.getWidth()/COL_SPAN);
        });
        
        timeSlot.getView().setOnMouseDragged((MouseEvent event) -> {
            TimeSlot hovered = timeSlotOver(event);
            if (hovered != null) {
                if (hovered != lastHovered && allowContinue(timeSlot, hovered)){
                    boolean movingDown = Math.abs(timeSlot.getRow() - hovered.getRow()) > 
                            Math.abs(timeSlot.getRow() - lastHovered.getRow());
                    if(movingDown) {
                        hovered.setSelected(!hovered.isSelected());
                    }else {
                        lastHovered.setSelected(!lastHovered.isSelected());
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
        
        timeSlot.getView().setOnMouseReleased((MouseEvent event) -> {
            // confirmed on doubleClick
            if (event.getClickCount() > 1) {
                Optional<ButtonType> result = confirmSelection(timeSlot);
                if (result.isPresent() && result.get() == ButtonType.OK) {
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
    
    private boolean fillBlanks(TimeSlot base, TimeSlot timeSlot) {
        /*
        This method aims to reduce errors caused by moving the mouse too fast
        If it detects a space between the last cell the mouse has hovered over
        and the current cell, it will fill in the space (checking to see if there
        are any booked cells within.
        
        There is still a bug present, movement in a single direction is now solved
        but if the mouse moves in both directions spaces will occur.
        
        The important part is that the bug is only visual, despite there being holes
        in the selection as soon as the mouse is released they dissapear (as the time
        slots are correct).
        
        I still would like to have it solved.
        */
        if(Math.abs(base.getRow() - timeSlot.getRow())  <
            Math.abs(base.getRow() - lastHovered.getRow())) {
            lastHovered.setSelected(!lastHovered.isSelected());
        }
        boolean res = true;
        TimeSlot currTS = null;
        int i;
        int sign = (int)Math.signum(timeSlot.getRow()-lastHovered.getRow());
        for (i = lastHovered.getRow() + sign; res && i != timeSlot.getRow(); i+=sign) {
            currTS = timeSlots.get(pressedCol-1).get(i);
            res = !currTS.isBooked();
            if(res) {
                currTS.setSelected(!currTS.isSelected());
            }
        }
        if(res){
            lastHovered=timeSlots.get(pressedCol-1).get(i - sign);
        }
        return res;
    }
    
    private void resetTimeSlots() {
        timeSlots.forEach(currDay -> {
            currDay.forEach(slot -> {
                slot.setSelected(false);
            });
        });
    }

    private TimeSlot timeSlotOver(MouseEvent event) { 
        Pane pane = (Pane)(event.getSource());//unnecessary but theres a bug in grid.getHeight() I don't understand
        Bounds paneBounds = pane.localToScene(pane.getBoundsInLocal());
        gridBounds = timeTable.localToScene(timeTable.getBoundsInLocal());
        int row = (int)((event.getSceneY()-gridBounds.getMinY())/(paneBounds.getHeight()));
        TimeSlot slot = timeSlots.get(pressedCol-1).get(row);
        return (slot.isBooked()) ? null : slot;
    }

    private Optional<ButtonType> confirmSelection(TimeSlot timeSlot) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("SlotTime");
        alerta.setHeaderText("Confirma la selecció");
        alerta.setContentText("Has seleccionat: "
                + timeSlot.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + ", "
                + timeSlot.getTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
        Optional<ButtonType> result = alerta.showAndWait();
        return result;
    }
}

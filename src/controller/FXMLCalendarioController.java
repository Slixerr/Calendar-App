package controller;

import application.AsignaturaCell;
import application.AlumnoCell;
import application.CalendarioIPC;
import static application.CalendarioIPC.SLOTS_FIRST;
import static application.CalendarioIPC.SLOTS_LAST;
import static application.CalendarioIPC.SLOT_LENGTH;
import application.DayPickerCell;
import application.Position;
import application.TimeSlot;
import static application.TimeSlot.BOTTOM;
import static application.TimeSlot.EMPTY;
import static application.TimeSlot.MIDDLE;
import static application.TimeSlot.TOP;
import application.TransparentPickerSkin;
import application.Week;
import com.sun.javafx.scene.control.skin.DatePickerContent;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import static controller.FXMLAlumnoController.CREAR;
import static controller.FXMLAlumnoController.MODIFICAR;
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
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Transform;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.text.View;
import referencias.accesoBD.AccesoBD;
import referencias.modelo.Alumno;
import referencias.modelo.Asignatura;
import referencias.modelo.Tutoria;
import referencias.modelo.Tutoria.EstadoTutoria;
import referencias.modelo.Tutorias;

public class FXMLCalendarioController implements Initializable {

    @FXML
    public DatePicker dayPicker;
    public static DatePicker dayPickerStatic; // A more correct solution would be to not create this through fxml
    public static List<DayPickerCell> cells = new ArrayList<DayPickerCell>();
    public static List<LocalDate> days = new ArrayList<LocalDate>();
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
    
    public static final int DISABLED=0,ALUMNOS=1,ASIGNATURAS=2;
    public static final PseudoClass MID_PSEUDO_CLASS = PseudoClass.getPseudoClass("mid");
    public static final PseudoClass BOOKED_PSEUDO_CLASS = PseudoClass.getPseudoClass("booked");
    public static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    public static final PseudoClass TOP_PSEUDO_CLASS = PseudoClass.getPseudoClass("topStack");
    public static final PseudoClass MIDDLE_PSEUDO_CLASS = PseudoClass.getPseudoClass("middleStack");
    public static final PseudoClass BOTTOM_PSEUDO_CLASS = PseudoClass.getPseudoClass("bottomStack");
    
    private int pressedCol = 0;
    private TimeSlot lastHovered = null;
    private Label slotSelected;
    
    private static Tutoria createdTut;
    private static Tutoria desctiptionBoxTut;
    private static Alumno createdAlumno;
    private static Asignatura createdAsignatura;
    
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

    private List<List<TimeSlot>> timeSlots = new ArrayList<>(); //Para varias columnas List<List<TimeSlot>>

    private ObjectProperty<LocalDateTime[]> bookingTime;//refactor to class

    private List<Label> diasSemana;
    
    private Bounds gridBounds;
    
    private Tutorias tutorias;
    
    private List<Tutoria> weekTutorias;
    
    private final BooleanProperty descriptionShowing = new SimpleBooleanProperty();
    
    private Node baseView = null;
    
    private final IntegerProperty sideBoxState = new SimpleIntegerProperty();
    
    private static Scene scene;
    @FXML
    private SplitPane sidePane;
    @FXML
    private VBox asignaturasBox;
    @FXML
    private VBox alumnosBox;
    @FXML
    private ListView<Asignatura> asignaturasLV;
    @FXML
    private ListView<Alumno> alumnosLV;
    
    private ObservableList<Asignatura> asignaturas = null;
    private ObservableList<Alumno> alumnos = null;
    @FXML
    private Button añadirAsignaturaButton;
    @FXML
    private Button añadirAlumnoButton;
    @FXML
    private ChoiceBox<EstadoTutoria> stateBox;
    
    private ResourceBundle bundle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        dayPickerStatic = dayPicker;
        dayPicker.setSkin(new TransparentPickerSkin(dayPicker));
        accessDatabase();
        generateTestParameters();
        addDayLabels();
        createSidebarListener();
        createBoundsListener();
        createBookingListener();
        createDayListener();
        createDescriptionListener();
        createAddingListener();
    }
    
    private void generateTestParameters() {
        Alumno daniel = new Alumno("Daniel", "Santamarina Puertas", "danielsantamarinapuertas@gmail.com");
        tutorias.getAlumnosTutorizados().add(daniel);
        daniel.setHeadShot(new Image(getClass().getResourceAsStream("/resources/daniel.jpg")));
        Alumno silviu = new Alumno("Silviu Valentin", "Manolescu", "silviu1200@gmail.com");
        //silviu.setHeadShot();
        tutorias.getAlumnosTutorizados().add(silviu);
        tutorias.getAsignaturas().add(new Asignatura("1224","IPC"));
        tutorias.getAsignaturas().add(new Asignatura("2334","TAL"));
    }
    
    public void accessDatabase() {
        tutorias = AccesoBD.getInstance().getTutorias();
    }
    
    private void addDayLabels() {
        diasSemana= new ArrayList<>();
        diasSemana.add(lunesCol);
        diasSemana.add(martesCol);
        diasSemana.add(miercoles);
        diasSemana.add(juevesCol);
        diasSemana.add(viernesCol);
    }
    
    private void createSidebarListener() {
        sideBoxState.set(DISABLED);
        sidePane.scaleXProperty().bind(Bindings.min(1, sideBoxState));
        sidePane.managedProperty().bind(Bindings.notEqual(sideBoxState, DISABLED));
        asignaturasBox.visibleProperty().bind(Bindings.equal(sideBoxState, ASIGNATURAS));
        asignaturasBox.disableProperty().bind(Bindings.notEqual(sideBoxState, ASIGNATURAS));
        alumnosBox.visibleProperty().bind(Bindings.equal(sideBoxState, ALUMNOS));
        alumnosBox.disableProperty().bind(Bindings.notEqual(sideBoxState, ALUMNOS));
        
        bindSidePaneLists();
        
        sideBoxState.addListener((a,b,c) -> {
            alumnosLV.getSelectionModel().clearSelection();
            asignaturasLV.getSelectionModel().clearSelection();
        });
    }
    
    private void bindSidePaneLists() {
        alumnos = tutorias.getAlumnosTutorizados();
        alumnosLV.setItems(alumnos);
        alumnosLV.setCellFactory(c -> new AlumnoCell());
        alumnosLV.setOnMouseExited((MouseEvent event) -> {
            alumnosLV.getSelectionModel().clearSelection();
        });
        
        asignaturas = tutorias.getAsignaturas();
        asignaturasLV.setItems(asignaturas);
        asignaturasLV.setCellFactory(c -> new AsignaturaCell());
        asignaturasLV.setOnMouseExited((MouseEvent event) -> {
            asignaturasLV.getSelectionModel().clearSelection();
        });
    }
    
    private void createBoundsListener() {
        timeTable.boundsInLocalProperty().addListener((a,b,c) -> {
            gridBounds = timeTable.localToScene(c);
        });
        timeTable.localToSceneTransformProperty().addListener((a,b,c) -> { 
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
    
    private void createAddingListener() {
        añadirAlumnoButton.setOnAction(a -> {
            alumnosLV.getSelectionModel().clearSelection();
            Alumno al = createAlumno("","","",null, CREAR);
            boolean cont = tutorias.getAlumnosTutorizados().contains(al);
            if(!cont && al != null) {tutorias.getAlumnosTutorizados().add(al);}
        });
        añadirAsignaturaButton.setOnAction(a -> {
            asignaturasLV.getSelectionModel().clearSelection();
            Asignatura as = createAsignatura("","",CREAR);
            boolean cont = tutorias.getAsignaturas().contains(as);
            if(!cont && as != null) {tutorias.getAsignaturas().add(as);}
        });
    }
    
    private void createDescriptionListener() {
        descriptionShowing.set(false);
        descriptionBox.disableProperty().bind(Bindings.not(descriptionShowing));
        descriptionBox.visibleProperty().bind(descriptionShowing);
        stateBox.setItems(FXCollections.observableArrayList(EstadoTutoria.options()));
    }
    
    private void bindDescriptionTo(Tutoria tut) {
        descriptionLabel.textProperty().bind(tut.anotacionesProperty());
        subjectLabel.textProperty().bind(tut.asignaturaProperty().asString());
        studentsLabel.textProperty().set(tut.getAlumnos().stream().map(Alumno::toString).collect(Collectors.joining("\n")));
        stateBox.setValue(tut.getEstado());
        tut.estadoProperty().bind(stateBox.valueProperty());
    }
    
    private void unbindDescription(Tutoria tut) {
        descriptionLabel.textProperty().unbind();
        subjectLabel.textProperty().unbind();
        tut.estadoProperty().unbind();
    }
    
    private void moveDescriptionBox(Bounds bounds) {
        double baseX = (bounds.getMaxX()+descriptionBox.getWidth() > scene.getWidth()) ? 
                bounds.getMinX() - descriptionBox.getWidth() : bounds.getMaxX();
        double baseY = (bounds.getMinY()+descriptionBox.getHeight() > scene.getHeight()) ? 
                bounds.getMaxY() - descriptionBox.getHeight() : bounds.getMinY();
        descriptionBox.setTranslateX(baseX-descriptionBox.getLayoutX());
        descriptionBox.setTranslateY(baseY-descriptionBox.getLayoutY());
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
        dayPicker.setDayCellFactory(c -> new DayPickerCell());
        dayPicker.setOnHidden(c->{
            cells = new ArrayList<>();
            days = new ArrayList<>();
        });
        
        dayPicker.valueProperty().addListener((a, b, c) -> {
            resetPastParameters();
            
            clearTimeTable();
            
            Week week = new Week(c);
        
            findTutorias(week);
            
            updateTimeTable(week);
            
            addLabels(week);
        });
        dayPicker.setValue(LocalDate.now());
    }

    private void resetPastParameters() {
        descriptionShowing.set(false);
        timeTable.getChildren().remove(slotSelected);
        slotSelected = null;
    }

    private void findTutorias(Week now) {
        weekTutorias = tutorias.getTutoriasConcertadas().stream()
                .filter((Tutoria tutoria) -> now.contains(tutoria.getFecha()))
                .collect(Collectors.toList());
    }

    private void updateTimeTable(Week now) {
        int dayIndex = 0;
        for (LocalDate day = now.getStartOfWeek(); 
                !day.isAfter(now.getEndOfWeek()); 
                day = day.plusDays(1), dayIndex++) {
            
            diasSemana.get(dayIndex).setText(bundle.getString(day.getDayOfWeek().toString())+System.lineSeparator()+day.toString());
            fillDaySlots(day, dayIndex);
        }
    }
    
    private void addLabels(Week now) {
        weekTutorias.forEach(tutoria -> {
            Label l = new Label();
            l.setPadding(new Insets(0,0,0,8));
            LocalDateTime start = LocalDateTime.of(tutoria.getFecha(),tutoria.getInicio());
            LocalDateTime end = start.plus(tutoria.getDuracion());
            l.setText(start.format(timeFormatter)+" - "+end.format(timeFormatter)+
                    "  "+tutoria.getAsignatura().getCodigo());
            l.setMouseTransparent(true);
            
            int col = (int)(Period.between(now.getStartOfWeek(), tutoria.getFecha()).getDays());
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
        if (startTime.getMinute()%30 == 0) timeSlot.isTopLine();
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

    private void registerHandlers(TimeSlot timeSlot) {// cut to MouseHandlerCreator
        createOnMousePressedHandler(timeSlot);
        
        createOnMouseDraggedHandler(timeSlot);
        
        createOnMouseReleasedHandler(timeSlot);
    }

    private void createOnMousePressedHandler(TimeSlot timeSlot) {
        timeSlot.getView().setOnMousePressed((MouseEvent event) ->{
            if(slotSelected==null){
                slotSelected = new Label();
                slotSelected.setPadding(new Insets(0,0,0,8));
                slotSelected.setMouseTransparent(true);
            }
            resetTimeSlots();
            if(!timeSlot.isBooked()){
                descriptionShowing.set(false);
                
                pressedCol = (int)((event.getSceneX()-gridBounds.getMinX())/((timeTable.getWidth())/CalendarioIPC.COL_SPAN));
                timeSlot.setSelected(true);
                timeSlot.setState(MIDDLE);
                
                timeSlot.blockSelected();
                lastHovered = timeSlot;

                bookingTime.setValue(new LocalDateTime[]{timeSlot.getStart(),timeSlot.getEnd()});
            }else{
                timeTable.getChildren().remove(slotSelected);
                slotSelected = null;
                
                activateDescription(timeSlot);
            }
        });
    }

    private void activateDescription(TimeSlot timeSlot) {
        Tutoria tutoria = timeSlot.getTutoria();
        bindDescriptionTo(tutoria);
        Week now = new Week(dayPicker.getValue());
        int col = (int)(Period.between(now.getStartOfWeek(), tutoria.getFecha()).getDays());
        int row = (int)(Duration.between(SLOTS_FIRST, tutoria.getInicio()).toMinutes()/SLOT_LENGTH.toMinutes());
        TimeSlot base = timeSlots.get(col).get(row);
        
        ChangeListener<Bounds> boundsListener = (a,b,c) -> {
            moveDescriptionBox(baseView.localToScene(c));
        };
        
        ChangeListener<Transform> transformListener = (a,b,c) -> {
            moveDescriptionBox(baseView.localToScene(baseView.getBoundsInLocal()));
        };
        
        if (baseView != null) {
            baseView.boundsInLocalProperty().removeListener(boundsListener);
            baseView.localToParentTransformProperty().removeListener(transformListener);
        }
        
        baseView = base.getView();
        
        moveDescriptionBox(baseView.localToScene(baseView.getBoundsInLocal()));
        
        baseView.boundsInLocalProperty().addListener(boundsListener);
        baseView.localToSceneTransformProperty().addListener(transformListener);
    }

    private void createOnMouseDraggedHandler(TimeSlot timeSlot) {
        timeSlot.getView().setOnMouseDragged((MouseEvent event) -> {
            if(lastHovered!=null){
                TimeSlot hovered = timeSlotOver(event);
                if (hovered != null && hovered != lastHovered && timeSlot.inHour(hovered) && allowContinue(timeSlot, hovered)){
                    boolean movingDown = Math.abs(timeSlot.getRow() - hovered.getRow()) > 
                            Math.abs(timeSlot.getRow() - lastHovered.getRow());
                    int cmp = hovered.compareRows(timeSlot);
                    int roundPos = (cmp>0)?BOTTOM:TOP;
                    if(movingDown) {
                        hovered.setSelected(true);
                        hovered.setState(roundPos);
                        lastHovered.setState((lastHovered == timeSlot) ? (cmp>0)?TOP:BOTTOM : EMPTY);
                    }else {
                        lastHovered.setSelected(false);
                        lastHovered.setState(EMPTY);
                        hovered.setState((hovered == timeSlot) ? MIDDLE : roundPos);
                    }
                    lastHovered = hovered;
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
            if (lastHovered != null) {
                Tutoria result = createTutoria(timeSlot, lastHovered);
                if (result != null) {
                    boolean order = lastHovered.getRow() > timeSlot.getRow();
                    int max = order ? lastHovered.getRow() : timeSlot.getRow();
                    int min = !order ? lastHovered.getRow() : timeSlot.getRow();
                    for (int i = min; i <= max; i++) {// not sure
                        TimeSlot curr = timeSlots.get(pressedCol).get(i);
                        curr.setTutoria(result);
                        curr.setSelected(false);
                        if(min == max) {curr.setState(MIDDLE);}
                        else {
                            if(i == min) curr.setState(TOP);
                            else if (i == max) curr.setState(BOTTOM);
                            else  curr.setState(EMPTY);
                        }
                    }
                    slotSelected.setText(result.getAsignatura().getCodigo()+"    "+slotSelected.getText());
                    slotSelected = new Label();
                    slotSelected.setPadding(new Insets(0,0,0,8));
                    slotSelected.setMouseTransparent(true);
                    
                    weekTutorias.add(result);
                    tutorias.getTutoriasConcertadas().add(result);
                    resetTimeSlots();
                }
                lastHovered = null;
            } else {
                unbindDescription(timeSlot.getTutoria());
                descriptionShowing.set(true);
                setModifiedTutoria(timeSlot.getTutoria());
            }
        });
    }
        
    private boolean allowContinue(TimeSlot base, TimeSlot currTS) {
        boolean res = base.inHour(currTS) && Math.abs(currTS.getRow() - lastHovered.getRow()) < 2;
        if(res) return true;
        res = fillBlanks(base, currTS);
        return res;
    }
    
    /*
    This method aims to reduce errors caused by moving the mouse too fast
    If it detects a space between the last cell the mouse has hovered over
    and the current cell, it will fill in the space (checking to see if there
    are any booked cells within.
    
    With the 1hour restriction on tutorías this method is pretty much obsolete,
    as it is practically impossible for someone to make a jump without hitting
    the borders. It took me 4 hours to code and debug this :(
    
    Regardless, I hold confort in the notion that if the time limit of 1 hour
    were to be extended or removed, this method would be of use to the program.
    */
    private boolean fillBlanks(TimeSlot base, TimeSlot currentSlot) {
        boolean res = true;
        int i;
        int sign = (int)Math.signum(currentSlot.getRow()-lastHovered.getRow());
        lastHovered.setState(EMPTY);
        
        TimeSlot currTS;
        for (i = lastHovered.getRow() + sign; res && i != currentSlot.getRow(); i+=sign) {
            currTS = timeSlots.get(pressedCol).get(i);
            res = !currTS.isBooked() && base.inHour(currTS);
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
        
        lastHovered=timeSlots.get(pressedCol).get(i-sign);
        return res;
    }
    
    public void resetTimeSlots() {
        timeSlots.forEach(currDay -> {
            currDay.forEach(slot -> {
                slot.setSelected(false);
            });
        });
    }

    private TimeSlot timeSlotOver(MouseEvent event) {
        Pane pane = (Pane)(event.getSource());//unnecessary but theres a bug in grid.getHeight() I don't understand
        Bounds paneBounds = pane.localToScene(pane.getBoundsInLocal());
        int row = (int)((event.getSceneY()-gridBounds.getMinY())/(paneBounds.getHeight()));
        TimeSlot slot;
        try{
            slot = timeSlots.get(pressedCol).get(row);
        }catch(Exception e) {
            slot = null;
        }
        return (slot == null || slot.isBooked()) ? null : slot;
    }

    private Tutoria createTutoria(TimeSlot start, TimeSlot end) {//make static
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLTutoria.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ignored) {}
        
        FXMLTutoriaController controller = loader.getController();
        
        controller.startVariables(start, end);

        Scene currScene = new Scene(root);
        Stage ventana2= new Stage();
        ventana2.initModality(Modality.APPLICATION_MODAL);
        ventana2.initStyle(StageStyle.UNDECORATED);
        ventana2.setScene(currScene);
        ventana2.showAndWait();
        
        return getTutoria();
    }
    
    private void editTutoria() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLModification.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ignored) {}
        
        FXMLModificationController controller = loader.getController();
        
        Scene currScene = new Scene(root);
        Stage ventana2= new Stage();
        ventana2.initModality(Modality.APPLICATION_MODAL);
        ventana2.initStyle(StageStyle.UNDECORATED);
        ventana2.setScene(currScene);
        unbindDescription(getModifiedTutoria());
        controller.startVariables(getModifiedTutoria());
        ventana2.showAndWait();
        bindDescriptionTo(getModifiedTutoria());
    }
    
    public static Alumno createAlumno(String sName, String sSurname, String sEmail, Image hs, int tipo) {
        FXMLLoader loader = new FXMLLoader(FXMLCalendarioController.class.getResource("/view/FXMLAlumno.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ignored) {}
        
        FXMLAlumnoController controller = loader.getController();
        
        Scene currScene = new Scene(root);
        Stage ventana2= new Stage();
        ventana2.initModality(Modality.APPLICATION_MODAL);
        ventana2.initStyle(StageStyle.UNDECORATED);
        ventana2.setScene(currScene);
        controller.setName(sName);
        controller.setSurname(sSurname);
        controller.setEmail(sEmail);
        controller.setHeadshot(hs);
        controller.setStage(ventana2);
        controller.setType(tipo);
        ventana2.showAndWait();

        return createdAlumno;
    }
    
    public static Asignatura createAsignatura(String description, String code, int type) {
        FXMLLoader loader = new FXMLLoader(FXMLCalendarioController.class.getResource("/view/FXMLAsignatura.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ignored) {}
        
        FXMLAsignaturaController controller = loader.getController();
        
        Scene currScene = new Scene(root);
        Stage ventana2 = new Stage();
        ventana2.initModality(Modality.APPLICATION_MODAL);
        ventana2.initStyle(StageStyle.UNDECORATED);
        ventana2.setScene(currScene);
        controller.setDescription(description);
        controller.setCode(code);
        controller.setType(type);
        ventana2.showAndWait();
        
        return createdAsignatura;
    }
    
    public static void setTutoria(Tutoria tut) {
        createdTut = tut;
    }
    
    public static Tutoria getTutoria() {
        return createdTut;
    }

    public static Tutoria getModifiedTutoria() {
        return desctiptionBoxTut;
    }

    public static void setModifiedTutoria(Tutoria modifiedTut) {
        FXMLCalendarioController.desctiptionBoxTut = modifiedTut;
    }
    
    public static void setCreatedAlumno(Alumno alumno) {
        createdAlumno = alumno;
    }
    
    static void setCreatedAsignatura(Asignatura asignatura) {
        createdAsignatura = asignatura;
    }
    
    public static void setScene(Scene sc) {
        scene = sc;
    }
    
    @FXML
    private void alumnoMethod(ActionEvent event) {
        sideBoxState.set((sideBoxState.get() == ALUMNOS) ? DISABLED : ALUMNOS);
    }

    @FXML
    private void subjectsMethod(ActionEvent event) {
        sideBoxState.set((sideBoxState.get() == ASIGNATURAS) ? DISABLED : ASIGNATURAS);
    }


    @FXML
    private void editMethod(ActionEvent event) {
        editTutoria();
    }

    @FXML
    private void closeMethod(ActionEvent event) {
        descriptionShowing.set(false);
    }

    @FXML
    private void helpMethod(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXMLHelp.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage ventana= new Stage();
        ventana.setMinHeight(500);
        ventana.setMinWidth(700);
        ventana.setTitle("Ventana Ayuda");
        ventana.setScene(scene);
        ventana.showAndWait();
    }

    @FXML
    private void previousWeekMethod(ActionEvent event) {
        dayPicker.setValue(dayPicker.getValue().minusDays(7));
    }

    @FXML
    private void nextWeekMethod(ActionEvent event) {
        dayPicker.setValue(dayPicker.getValue().plusDays(7));
    }
}

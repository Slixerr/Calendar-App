package application;

import static controller.FXMLAlumnoController.MODIFICAR;
import controller.FXMLCalendarioController;
import static controller.FXMLCalendarioController.createAlumno;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import referencias.accesoBD.AccesoBD;
import referencias.modelo.Alumno;
import referencias.modelo.Asignatura;


public class AlumnoCell extends ListCellDeselect<Alumno>{
    private GridPane pane;
    private Label name;
    private Label mail;
    private ImageView headshot;
    private final double headshotSize = 45;
    private final Image defaultHS = new Image(getClass().getResourceAsStream("/resources/headshot.png"));
    private Button eliminar;
    private Button editar;
    
    private ColumnConstraints buttonCol;
    
    public AlumnoCell() {
        super();
        eliminar = new Button("X");
        editar = new Button("E");

        
        eliminar.setDisable(false);
        editar.setDisable(false);
        eliminar.setPadding(new Insets(1,2,1,2));
        editar.setPadding(new Insets(1,2,1,2));
        
        name = new Label();
        mail = new Label();
        pane = new GridPane();
        headshot = createFrame();
        buttonCol = new ColumnConstraints();
        
        Font small = Font.font(null, FontWeight.LIGHT,FontPosture.ITALIC,13);
        mail.fontProperty().setValue(small);
        
        pane.add(headshot,0,0);
        pane.add(eliminar, 1, 0);
        pane.add(editar, 1, 1);
        pane.add(name, 2, 0);
        pane.add(mail, 2, 1);
        GridPane.setConstraints(headshot, 0, 0, 1, 2);
        pane.getColumnConstraints().addAll(new  ColumnConstraints(), buttonCol);
        
        pane.setHgap(5);
        //pane.setGridLinesVisible(true);
        
        setText(null);
        setGraphic(pane);
    }
    
    @Override
    protected void updateItem(Alumno item, boolean empty) {
        super.updateItem(item, empty);
        
        if (!empty && item != null) {
            Image img = (item.getHeadShot() != null) ? item.getHeadShot():defaultHS;
            headshot.setImage(img);
            name.setText(item.getNombre() + " " + item.getApellidos());
            mail.setText(item.getEmail());
            bindButtons(item);
            
            setGraphic(pane);
        } else {
            eliminar.disableProperty().unbind();
            editar.visibleProperty().unbind();
            editar.disableProperty().unbind();
            editar.visibleProperty().unbind();
            buttonCol.percentWidthProperty().unbind();
            setGraphic(null);
        }
    }

    private void bindButtons(Alumno alumno) {
        eliminar.disableProperty().bind(Bindings.not(this.selectedProperty()));
        eliminar.visibleProperty().bind(this.selectedProperty());
        editar.disableProperty().bind(Bindings.not(this.selectedProperty()));
        editar.visibleProperty().bind(this.selectedProperty());
        buttonCol.maxWidthProperty().bind(
                Bindings.when(this.selectedProperty())
                        .then(USE_COMPUTED_SIZE)
                        .otherwise(0));
        ObservableList<Alumno> alumnos = AccesoBD.getInstance().getTutorias().getAlumnosTutorizados();
        editar.setOnAction((ActionEvent event) -> {
            Alumno alCreado = createAlumno(alumno.getNombre(), alumno.getApellidos(), alumno.getEmail(), alumno.getHeadShot(), MODIFICAR);
            if(alCreado != null) {
                int pos = alumnos.indexOf(alumno);
                alumnos.remove(alumno);
                alumnos.add(pos, alCreado);
            }
            this.getListView().getSelectionModel().clearSelection();
        });
        eliminar.setOnAction((ActionEvent event) -> {
            AccesoBD.getInstance().getTutorias().getAlumnosTutorizados().remove(alumno);
            this.getListView().getSelectionModel().clearSelection();
        });
    }
    
    private ImageView createFrame() {
        ImageView frame = new ImageView();
        frame.setFitHeight(headshotSize*1.245);
        frame.setFitWidth(headshotSize);
        
        Circle clip = new Circle(headshotSize/2);
        clip.setLayoutX(headshotSize/2);
        clip.setLayoutY(headshotSize*1.245/2);
        
        frame.setClip(clip);
        return frame;
    }
}
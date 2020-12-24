package application;

import static de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon.DELETE;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import referencias.modelo.Alumno;

public class SimpleAlumnoCell extends ListCell<Alumno>{
    private final HBox pane;
    private final Label name;
    private final Button eliminar;
    private static final PseudoClass BUTTON_PSEUDO_CLASS = PseudoClass.getPseudoClass("button-popup");
    
    //private final ColumnConstraints buttonCol;
    
    public SimpleAlumnoCell() {
        super();
        eliminar = new Button();
        MaterialDesignIconView icon = new MaterialDesignIconView(DELETE);
        icon.setSize("15");
        eliminar.setGraphic(icon);
        eliminar.setTooltip(new Tooltip("Eliminar"));
        eliminar.pseudoClassStateChanged(BUTTON_PSEUDO_CLASS, true);
        
        setOnMouseEntered((MouseEvent event) -> {
            getListView().getSelectionModel().select(getItem());
        });

        eliminar.setDisable(false);
        eliminar.setPadding(new Insets(1,2,1,2));
        
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(hb, Priority.ALWAYS);
        hb.getChildren().add(eliminar);
        
        name = new Label();
        pane = new HBox();
        pane.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(pane, Priority.ALWAYS);
        pane.setSpacing(5);
        //buttonCol = new ColumnConstraints();
        pane.getChildren().add(name);
        pane.getChildren().add(hb);
        //pane.getColumnConstraints().addAll(new  ColumnConstraints(), buttonCol);
        
        //pane.setHgap(5);
        
        setText(null);
        setGraphic(pane);
    }
    
    @Override
    protected void updateItem(Alumno item, boolean empty) {
        super.updateItem(item, empty);
        
        if (!empty && item != null) {
            name.setText(item.getNombre() + " " + item.getApellidos());
            bindButtons(item);
            
            setGraphic(pane);
        } else {
            //eliminar.disableProperty().unbind();
            //buttonCol.percentWidthProperty().unbind();
            setGraphic(null);
        }
    }

    private void bindButtons(Alumno alumno) {
        /*eliminar.disableProperty().bind(Bindings.not(this.selectedProperty()));
        eliminar.visibleProperty().bind(this.selectedProperty());
        buttonCol.maxWidthProperty().bind(
                Bindings.when(this.selectedProperty())
                        .then(USE_COMPUTED_SIZE)
                        .otherwise(0));*/
        
        eliminar.setOnAction((ActionEvent event) -> {
            this.getListView().getSelectionModel().clearSelection();
            this.getListView().getItems().remove(alumno);
        });
    }
}

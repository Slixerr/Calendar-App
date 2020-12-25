package application;

import static controller.FXMLAlumnoController.MODIFICAR;
import static controller.FXMLCalendarioController.createAsignatura;
import static de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon.DELETE;
import static de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon.PENCIL;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import referencias.accesoBD.AccesoBD;
import referencias.modelo.Asignatura;

public class AsignaturaCell extends ListCell<Asignatura> {
    private final GridPane pane;
    private final Label name;
    private final Label code;
    private final Button eliminar;
    private final Button editar;
    
    private final ColumnConstraints buttonCol;
    
    public AsignaturaCell() {
        super();
        MaterialDesignIconView delete = new MaterialDesignIconView(DELETE);
        delete.setSize("18");
        eliminar = new Button("", delete);
        eliminar.setTooltip(new Tooltip("Eliminar"));
        
        MaterialDesignIconView pencil = new MaterialDesignIconView(PENCIL);
        pencil.setSize("18");
        editar = new Button("", pencil);
        editar.setTooltip(new Tooltip("Editar"));
        

        
        setOnMouseEntered((MouseEvent event) -> {
            getListView().getSelectionModel().select(getItem());
        });

        eliminar.setDisable(false);
        editar.setDisable(false);
        eliminar.setPadding(new Insets(1,2,1,2));
        editar.setPadding(new Insets(1,2,1,2));
        
        name = new Label();
        code = new Label();
        pane = new GridPane();
        buttonCol = new ColumnConstraints();
        
        Font small = Font.font(null, FontWeight.LIGHT,FontPosture.ITALIC,13);
        code.fontProperty().setValue(small);
        
        pane.add(eliminar, 0, 0);
        pane.add(editar, 0, 1);
        pane.add(name, 1, 1);
        pane.add(code, 1, 0);
        pane.getColumnConstraints().addAll(buttonCol);
        
        pane.setHgap(5);
        //pane.setGridLinesVisible(true);
        
        setText(null);
        setGraphic(pane);
    }
    
    @Override
    protected void updateItem(Asignatura item, boolean empty) {
        super.updateItem(item, empty);
        
        if (!empty && item != null) {
            name.setText(item.getDescripcion());
            code.setText(item.getCodigo());
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

    private void bindButtons(Asignatura asignatura) {
        eliminar.disableProperty().bind(Bindings.not(this.selectedProperty()));
        eliminar.visibleProperty().bind(this.selectedProperty());
        editar.disableProperty().bind(Bindings.not(this.selectedProperty()));
        editar.visibleProperty().bind(this.selectedProperty());
        buttonCol.maxWidthProperty().bind(
                Bindings.when(this.selectedProperty())
                        .then(USE_COMPUTED_SIZE)
                        .otherwise(0));
        ObservableList<Asignatura> asignaturas = AccesoBD.getInstance().getTutorias().getAsignaturas();
        editar.setOnAction((ActionEvent event) -> {
            createAsignatura(asignatura, MODIFICAR);
            name.setText(asignatura.getDescripcion());
            code.setText(asignatura.getCodigo());
            this.getListView().getSelectionModel().clearSelection();
        });
        eliminar.setOnAction((ActionEvent event) -> {
            asignaturas.remove(asignatura);
            AccesoBD.getInstance().getTutorias().fixInstancesOf(asignatura);
            this.getListView().getSelectionModel().clearSelection();
        });
    }
}

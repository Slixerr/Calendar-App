package application;

import javafx.scene.control.ListCell;
import referencias.modelo.Asignatura;

public class SimpleSubjectCell extends ListCell<Asignatura>{

    @Override
    protected void updateItem(Asignatura item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {
            setText("");
        }
        else {
            setText(item.getCodigo());
        }
    }
}

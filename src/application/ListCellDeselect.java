package application;

import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;

/*
The purpose of this class is to allow items in a listView to be deselected just
by clicking on them

This is a hacky solution, if this were a long term project intended to work 
without updates it would be removed. Since the javafx listView gives no hooks 
for managing its selection internally it must be done using external variables.

If the selection event were to change to another function (ej:mousePressed) 
in a future javafx update, the method would be rendered obsolete, which is 
why it is by no means long-term stable.
*/
public class ListCellDeselect<T> extends ListCell<T>{
    private boolean wasSelected = false;
    public ListCellDeselect() {
        super();
        setOnMouseClicked((MouseEvent event) -> {
            if (wasSelected && isSelected()) {
                wasSelected = false;
                getListView().getSelectionModel().clearSelection();
            } else {
                wasSelected = isSelected();
            }
        });
    }
    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);
        if(!isSelected()) wasSelected = false;
    }
}

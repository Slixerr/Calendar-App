/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import javafx.scene.control.ListCell;
import referencias.modelo.Asignatura;

/**
 *
 * @author danie
 */
public class AsignaturaCell extends ListCell<Asignatura>{
    @Override
    protected void updateItem(Asignatura item, boolean empty) {
        super.updateItem(item, empty);
        setText((empty || item == null) ? "" : item.getDescripcion());
    }
}

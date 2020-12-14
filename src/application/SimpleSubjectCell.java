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
 * @author silvi
 */
public class SimpleSubjectCell extends ListCell<Asignatura>{

    @Override
    protected void updateItem(Asignatura item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if(empty || item == null) {
            setText("");
        }
        else {
            setText(item.getDescripcion());
        }
    }
}

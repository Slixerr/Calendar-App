/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import javafx.scene.control.ListCell;
import referencias.modelo.Alumno;

/**
 *
 * @author danie
 */
public class AlumnoCell extends ListCell<Alumno>{
    @Override
    protected void updateItem(Alumno item, boolean empty) {
        super.updateItem(item, empty);
        setText((empty || item == null) ? "" : item.getNombre() + " " + item.getApellidos());
    }
}

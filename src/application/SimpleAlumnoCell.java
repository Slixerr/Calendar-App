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
 * @author silvi
 */
public class SimpleAlumnoCell extends ListCell<Alumno>{

    @Override
    protected void updateItem(Alumno item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if(empty || item == null) {
            setText("");
        }
        else {
            setText(item.getNombre() + " " + item.getApellidos());
        }
    }
}

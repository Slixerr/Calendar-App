/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import referencias.modelo.Alumno;
import referencias.modelo.Asignatura;


public class AlumnoCell extends ListCell<Alumno>{
    @Override
    protected void updateItem(Alumno item, boolean empty) {
        super.updateItem(item, empty);
        
        if (!empty && item != null) {
            Image img;
            if(item.getHeadShot() != null) {
                img = item.getHeadShot();
            } else {
                img = new Image(getClass().getResourceAsStream("/resources/headshot.png"));
            }
            ImageView imageView = new ImageView(img);
            double size = 45;
            imageView.setFitHeight(size*1.245);
            imageView.setFitWidth(size);
            Circle clip = new Circle(size/2);
            clip.setLayoutX(size/2);
            clip.setLayoutY(size*1.245/2);
            imageView.setClip(clip);
            setGraphic(imageView);

            setText(item.getNombre() + " " + item.getApellidos()+"\n"+item.getEmail());
        } else {
            setGraphic(null);
            setText("");
        }
    }
}



    


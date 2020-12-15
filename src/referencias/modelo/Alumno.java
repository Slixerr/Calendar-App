/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package referencias.modelo;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

/**
 *
 * @author DSIC_jsoler
 */
public class Alumno {

    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty apellidos = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final ObjectProperty<Image> headshot = new SimpleObjectProperty<>();

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String value) {
        nombre.set(value);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos.get();
    }

    public void setApellidos(String value) {
        apellidos.set(value);
    }

    public StringProperty apellidosProperty() {
        return apellidos;
    }
    
    public ObjectProperty headshotProperty() {
        return headshot;
    }

    public Image getHeadShot() {
        return headshot.get();
    }

    public void setHeadShot(Image img) {
        headshot.set(img);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String value) {
        email.set(value);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public Alumno() {
    }
    
    public Alumno(String nom, String apell, String email) {
        this.nombre.set(nom);
        this.apellidos.set(apell);
        this.email.set(email);
    }

    @Override
    public String toString() {
        return nombre.get() + " " + apellidos.get();
    }
}

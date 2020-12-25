/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package referencias.modelo;

import java.io.File;
import java.net.URI;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author DSIC_jsoler
 */
public class Alumno {

    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty apellidos = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    
    private File imageAt;
    private final ObjectProperty<Image> headshot;
    
    public void setfileOfHS(File file) {
        imageAt = file;
    }

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
    
    public String getHeadshot() {
        return imageAt.toURI().toString();
    }
    
    public void setHeadshot(String uri) {
        imageAt = new File(URI.create(uri));
        try {
            headshot.set(new Image(uri));
        } catch (Exception ignore) {}
    }

    public Image headshotImage() {
        return headshot.get();
    }

    public void headshotImageTo(Image img) {
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
        headshot = new SimpleObjectProperty<>();
    }
    
    public Alumno(String nom, String apell, String email) {
        headshot = new SimpleObjectProperty<>();
        this.nombre.set(nom);
        this.apellidos.set(apell);
        this.email.set(email);
    }

    @Override
    public String toString() {
        return nombre.get() + " " + apellidos.get();
    }

    @Override
    public boolean equals(Object obj) {
        Alumno al = (Alumno)obj;
        boolean res;
        try{
            res = this.getNombre().equals(al.getNombre()) && 
                this.getApellidos().equals(al.getApellidos()) &&
                this.getEmail().equals(al.getEmail());
        } catch(NullPointerException e) {
            res = false;
        }
        return res;
    }
    
    
}

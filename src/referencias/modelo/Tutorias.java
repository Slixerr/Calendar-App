/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DSIC_jsoler
 */
@XmlRootElement(name = "MisTutorias")
public class Tutorias {

    private final ObservableList<Alumno> alumnosTutorizados = FXCollections.observableArrayList();
    private final ObservableList<Asignatura> asignaturas = FXCollections.observableArrayList();
    private final ObservableList<Tutoria> tutoriasConcertadas = FXCollections.observableArrayList(); 
    
    @XmlElement(name = "Alumno")
    public ObservableList<Alumno> getAlumnosTutorizados() {
        return alumnosTutorizados;
    }

    @XmlElement(name = "Tutorias")
    public ObservableList<Tutoria> getTutoriasConcertadas() {
        return tutoriasConcertadas;
    }


    @XmlElement(name = "Asignatura")
    public ObservableList<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    public Tutorias() {
    }
    
}

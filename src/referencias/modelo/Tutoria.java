/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package referencias.modelo;

import referencias.modelo.Alumno;
import java.time.Duration;
import modelo.util.LocalDateAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import modelo.util.DurationAdapter;
import modelo.util.LocalDateTimeAdapter;
import modelo.util.LocalTimeAdapter;

/**
 *
 * @author DSIC_jsoler
 */
public class Tutoria {

    @XmlType(name = "EstadoTutoria")
    @XmlEnum
    public static enum EstadoTutoria {
        PEDIDA, ANULADA, REALIZADA, NO_ASISTIDA;

        public String value() {
            return name();
        }

        public static EstadoTutoria fromValue(String v) {
            return valueOf(v);
        }
    }

    private final ObjectProperty<EstadoTutoria> estado = new SimpleObjectProperty<>();

    private ObjectProperty<LocalDate> fecha = new SimpleObjectProperty<>();
    
    private ObjectProperty<LocalTime> inicio = new SimpleObjectProperty<>();

    private final ObjectProperty<Duration> duracion = new SimpleObjectProperty();

    private StringProperty anotaciones = new SimpleStringProperty();

    private ObjectProperty<Asignatura> asignatura = new SimpleObjectProperty<>();

    private final ObservableList<Alumno> alumnos = FXCollections.observableArrayList();
    
    public static ObjectProperty<Tutoria> nullValue() {
        ObjectProperty<Tutoria> res = new SimpleObjectProperty<Tutoria>();
        res.set(null);
        return res;
    }

    @XmlElement(name = "Alumno")
    public ObservableList<Alumno> getAlumnos() {
        return alumnos;
    }

    public EstadoTutoria getEstado() {
        return estado.get();
    }

    public void setEstado(EstadoTutoria value) {
        estado.set(value);
    }

    public ObjectProperty estadoProperty() {
        return estado;
    }

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    public LocalDate getFecha() {
        return fecha.get();
    }

    public void setFecha(LocalDate value) {
        fecha.set(value);
    }

    public ObjectProperty fechaProperty() {
        return fecha;
    }
    
    @XmlJavaTypeAdapter(value = LocalTimeAdapter.class)
    public LocalTime getInicio() {
        return inicio.get();
    }

    public void setInicio(LocalTime value) {
        inicio.set(value);
    }

    public ObjectProperty inicioProperty() {
        return inicio;
    }

    public String getAnotaciones() {
        return anotaciones.get();
    }

    public void setAnotaciones(String value) {
        anotaciones.set(value);
    }

    public StringProperty anotacionesProperty() {
        return anotaciones;
    }

    @XmlJavaTypeAdapter(value = DurationAdapter.class)
    public Duration getDuracion() {
        return duracion.get();
    }

    public void setDuracion(Duration value) {
        duracion.set(value);
    }

    public ObjectProperty duracionProperty() {
        return duracion;
    }

    public Asignatura getAsignatura() {
        return asignatura.get();
    }

    public void setAsignatura(Asignatura value) {
        asignatura.set(value);
    }

    public ObjectProperty asignaturaProperty() {
        return asignatura;
    }

    public Tutoria() {
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package referencias.modelo;

import java.time.Duration;
import referencias.modelo.util.LocalDateAdapter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import referencias.modelo.util.DurationAdapter;
import referencias.modelo.util.LocalTimeAdapter;

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
        
        public static List<EstadoTutoria> options() {
            return Arrays.asList(values());
        }
        
        public String toString() {
            String res = "";
            switch(this) {
                case PEDIDA:
                  res = "Pedida";
                  break;
                case ANULADA:
                  res = "Anulada";
                  break;
                case REALIZADA:
                  res = "Realizada";
                  break;
                case NO_ASISTIDA:
                  res = "No asistida";
                  break;
            }
            return res;
        }
    }
    
    private final ObjectProperty<EstadoTutoria> estado = new SimpleObjectProperty<>();

    private final ObjectProperty<LocalDate> fecha = new SimpleObjectProperty<>();
    
    private final ObjectProperty<LocalTime> inicio = new SimpleObjectProperty<>();

    private final ObjectProperty<Duration> duracion = new SimpleObjectProperty();

    private final StringProperty anotaciones = new SimpleStringProperty();

    private final ObjectProperty<Asignatura> asignatura = new SimpleObjectProperty<>();

    private final ObservableList<Alumno> alumnos = FXCollections.observableArrayList();
    
    public static ObjectProperty<Tutoria> nullValue() {
        ObjectProperty<Tutoria> res = new SimpleObjectProperty<>();
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

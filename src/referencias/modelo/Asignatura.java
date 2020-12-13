/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package referencias.modelo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 *
 * @author DSIC_jsoler
 */
public class Asignatura {

    private final StringProperty codigo = new SimpleStringProperty();
    private final StringProperty descripcion = new SimpleStringProperty();
    
    public String getCodigo() {
        return codigo.get();
    }

    public void setCodigo(String value) {
        codigo.set(value);
    }

    public StringProperty codigoProperty() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String value) {
        descripcion.set(value);
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public Asignatura() {
    }
    public Asignatura(String cod, String desc) {
        this.codigo.set(cod);
        this.descripcion.set(desc);
    }

    @Override
    public String toString() {
        return descripcion.get();
    }

    
}

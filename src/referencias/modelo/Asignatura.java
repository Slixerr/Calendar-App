/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package referencias.modelo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import referencias.accesoBD.AccesoBD;
/**
 *
 * @author DSIC_jsoler
 */
public class Asignatura {

    private final StringProperty codigo = new SimpleStringProperty();
    private final StringProperty descripcion = new SimpleStringProperty();
    private int color;
    
    
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
        setColor();
    }
    public Asignatura(String cod, String desc) {
        this.codigo.set(cod);
        this.descripcion.set(desc);
        setColor();
    }

    @Override
    public String toString() {
        return descripcion.get();
    }

    void setColor() {
        int nAs = AccesoBD.getInstance().getTutorias().getAsignaturas().size()+1;
        int logVal = (int)(Math.floor(Math.log(nAs)/Math.log(2)));
        color = (int) ((255/(Math.pow(2,logVal+1))) * ((nAs-Math.pow(2, logVal)+1)*2 - 1));
    }
}

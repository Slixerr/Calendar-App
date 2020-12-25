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
    
    public Asignatura () {
    }

    public Asignatura(boolean calculateCol) {
        calculateCol();
    }
    
    public Asignatura(String cod, String desc) {
        this.codigo.set(cod);
        this.descripcion.set(desc);
        calculateCol();
    }

    @Override
    public String toString() {
        return descripcion.get();
    }

    void calculateCol() {
        int nAs = AccesoBD.getInstance().getTutorias().getAsignaturas().size()+1;
        int logVal = (int)(Math.floor(Math.log(nAs)/Math.log(2)));
        color = (int) ((255/(Math.pow(2,logVal+1))) * ((nAs-Math.pow(2, logVal)+1)*2 - 1));
    }
    
    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Asignatura)) return false;
        Asignatura as = (Asignatura)obj;
        return as.getCodigo().equals(this.getCodigo()) && as.getDescripcion().equals(this.getDescripcion());
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accesoBD;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import modelo.Tutorias;

/**
 *
 * @author jose
 */
public class AccesoBD {

    private final String path = System.getProperty("user.home") + "/tutorias.xml";

    private static boolean cargado = false;
    private static Tutorias misTutorias = null;

    //implementa el patron singleton
    private static AccesoBD bd;

    private AccesoBD() {
    }
     /**
     * Get the unique instance of AccesoBD
     *
     * @return the AccesoBD object
     */
    public static AccesoBD getInstance() {
        if (bd == null) {
            bd = new AccesoBD();
        }
        return bd;
    }
     /**
     * Get the object Gym saved in the XML file
     *
     * @return the Gym object
     */
    public Tutorias getTutorias() {
        if (!cargado) {
            misTutorias = null;
            try {
                String camino = path;
                File file = new File(camino);
                JAXBContext jaxbContext = JAXBContext.newInstance(Tutorias.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                misTutorias = (Tutorias) jaxbUnmarshaller.unmarshal(file);
            } catch (JAXBException e) {
               // e.printStackTrace();
                misTutorias= new Tutorias();
            }
            
            cargado = true;
        }
        return misTutorias;
    }
    /**
     * Save the object Gym in the XML file
     *
     */
    public void salvar() {
        if (misTutorias == null) {
            misTutorias = new Tutorias();
            cargado = true;
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Tutorias.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //Marshal the employees list in console
            jaxbMarshaller.marshal(misTutorias, System.out);

            //Marshal the employees list in file
            jaxbMarshaller.marshal(misTutorias, new File(path));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}

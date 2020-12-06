/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.util;

import java.time.Duration;
import java.time.LocalDate;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author DSIC_jsoler
 */
public class DurationAdapter extends XmlAdapter<String, Duration> {

    public Duration unmarshal(String v) throws Exception {
        return Duration.parse(v);
    }

    public String marshal(Duration v) throws Exception {
        return v.toString();
    }
}

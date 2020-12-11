/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.time.LocalDate;

/**
 *
 * @author danie
 */
public class Week {
    LocalDate startOfWeek;

    public LocalDate getStartOfWeek() {
        return startOfWeek;
    }

    public LocalDate getEndOfWeek() {
        return endOfWeek;
    }
    LocalDate endOfWeek;
    
    public Week(LocalDate date) {
        startOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        endOfWeek = startOfWeek.plusDays(4);
    }
    
}

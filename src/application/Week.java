package application;

import java.time.LocalDate;

public class Week {
    LocalDate startOfWeek;
    LocalDate endOfWeek;
    
    public Week(LocalDate date) {
        startOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        endOfWeek = startOfWeek.plusDays(4);
    }
    
    public LocalDate getStartOfWeek() {
        return startOfWeek;
    }

    public LocalDate getEndOfWeek() {
        return endOfWeek;
    }
    
    public boolean contains(LocalDate fecha) {
        return this.getStartOfWeek().isBefore(fecha) && this.getEndOfWeek().isAfter(fecha) ||
                    this.getStartOfWeek().isEqual(fecha) || this.getEndOfWeek().isEqual(fecha);
    }
}

package application;

import static controller.FXMLCalendarioController.dayPickerStatic;
import static controller.FXMLCalendarioController.cells;
import static controller.FXMLCalendarioController.days;
import java.time.DayOfWeek;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.DateCell;
import javafx.scene.input.MouseEvent;

public class DayPickerCell extends DateCell {
    
    private static final String CSS_WEEK_DAY = "week-day";
    private static final String CSS_WEEK_SELECTED = "week-selected";
    private static Week weekBase;
    private static Week weekHover;

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        
        weekBase = new Week(dayPickerStatic.getValue()); // can be optimized
        
        EventHandler<MouseEvent> mouseHandler = (MouseEvent event) -> {
            if (item.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    item.getDayOfWeek() != DayOfWeek.SUNDAY) {
                weekHover = new Week(item);
            } else {
                weekHover = new Week(item.minusDays(2));
            }// move functionality to week class
            
            for (int i = 0; i < days.size(); i++) {
                ObservableList<String> sc = cells.get(i).getStyleClass();
                if (weekHover != null && weekHover.contains(days.get(i)) && !weekBase.contains(item)) {
                    if(!sc.contains(CSS_WEEK_SELECTED)) sc.add(CSS_WEEK_SELECTED);
                } else {
                    sc.remove(CSS_WEEK_SELECTED);
                }
            }
        };
        setOnMouseEntered(mouseHandler);
        
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {   
            if (weekBase.contains(item)) getStyleClass().add(CSS_WEEK_DAY);
            cells.add(this);
            days.add(item); // should be refactored to object
        }
    }
}

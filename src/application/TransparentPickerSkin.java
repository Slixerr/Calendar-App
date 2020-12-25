package application;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.CALENDAR;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import static javafx.scene.paint.Color.WHITE;

public class TransparentPickerSkin extends DatePickerSkin {
        
    public TransparentPickerSkin(DatePicker dp) {
        super(dp);
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        StackPane pane = new StackPane();
        
        FontAwesomeIconView cal = new FontAwesomeIconView(CALENDAR);
        cal.setSize("20");
        cal.setFill(WHITE);
        pane.getChildren().add(cal);
        pane.setAlignment(Pos.CENTER);
        getChildren().remove(0);
        getChildren().add(0, pane);
        pane.resizeRelocate(w-25, 0, 20,h);
    }

}
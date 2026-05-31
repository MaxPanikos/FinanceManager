package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;

public class DateRangePopup extends DefaultPopup {
    @FXML
    private DatePicker fromPicker, toPicker;
    @FXML
    private Label responseLabel;

    @FXML
    public void initialize() {
        this.fromPicker.setValue(LocalDate.now().minusDays(1));
        this.toPicker.setValue(LocalDate.now());
    }

    public DateRangePopup(AppView appView) {
        super(appView);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("date-range-view.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * fxml call method only
     */
    @FXML
    private void saveSettings () {
        save(fromPicker, toPicker, responseLabel);
    }

    /**
     * saves amd passes picked date range
     * @param fromPicker from which date
     * @param toPicker to which date
     * @param responseLabel response label for errors
     */
    public void save (DatePicker fromPicker, DatePicker toPicker, Label responseLabel) {
        LocalDate from = fromPicker.getValue();
        LocalDate to = toPicker.getValue();
        if (from.isAfter(to)) {
            responseLabel.setText("Špatně zadané rozmezí");
            return;
        }
        appView.hidePopup();
    }
}

package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;

public class DateRangePopup extends VBox {
    private AppView appView;
    private TransactionView transactionView;
    @FXML
    private DatePicker fromPicker, toPicker;
    @FXML
    private Label responseLabel;

    @FXML
    public void initialize() {
        this.fromPicker.setValue(LocalDate.now().minusDays(1));
        this.toPicker.setValue(LocalDate.now());
    }

    public DateRangePopup(AppView appView, TransactionView transactionView) {
        super();
        this.appView = appView;
        this.transactionView = transactionView;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("date-range-view.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void saveSettings () {
        LocalDate from = fromPicker.getValue();
        LocalDate to = toPicker.getValue();
        if (from.isAfter(to)) {
            responseLabel.setText("Špatně zadané rozmezí");
            return;
        }
        transactionView.setRange(from, to);
        appView.hidePopup();
    }

    @FXML
    private void closePopup () {
        appView.hidePopup();
    }
}

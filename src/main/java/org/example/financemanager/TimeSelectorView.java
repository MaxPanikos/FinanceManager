package org.example.financemanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TimeSelectorView extends HBox {
    private Page page;
    private LocalDate fromDate, toDate;
    @FXML
    private ToggleGroup group;
    @FXML
    private ToggleButton oneMonthButton, threeMonthsButton, halfYearButton, oneYearButton, threeYearsButton, customRangeButton;


    @FXML
    private void initialize() {
    }

    public TimeSelectorView(Page page) {
        super();
        this.page = page;
        this.fromDate = LocalDate.now();
        this.toDate = LocalDate.now();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("time-selector-view.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSelected (ToggleButton button) {
        if (button.isSelected()) {
            oneMonthButton.setSelected(false);
        }
    }

    @FXML
    private void onButtonClicked(ActionEvent event) {
        LocalDate prevFromDate = fromDate;
        LocalDate prevToDate = toDate;
        ToggleButton button = (ToggleButton) event.getSource();
        if (button.isSelected()) {
            button.setSelected(true);
            return;
        }
        switch (button.getId()) {
            case "oneMonthButton":
                setSelected(oneMonthButton);
                fromDate = fromDate.minusMonths(1);
                toDate = LocalDate.now();
                break;
            case "threeMonthsButton":
                setSelected(threeMonthsButton);
                fromDate = fromDate.minusMonths(3);
                toDate = LocalDate.now();
                break;
            case "halfYearButton":
                setSelected(halfYearButton);
                fromDate = fromDate.minusMonths(6);
                toDate = LocalDate.now();
                break;
            case "oneYearButton":
                setSelected(oneYearButton);
                fromDate = fromDate.minusYears(1);
                toDate = LocalDate.now();
                break;
            case "threeYearsButton":
                setSelected(threeYearsButton);
                fromDate = fromDate.minusYears(3);
                toDate = LocalDate.now();
                break;
            case "customRangeButton":
                setSelected(customRangeButton);
                page.getAppView().showPopup(new DateRangePopup(page.getAppView(), page){
                    @Override
                    public void save (DatePicker fromPicker, DatePicker toPicker, Label responseLabel) {
                        LocalDate from = fromPicker.getValue();
                        LocalDate to = toPicker.getValue();
                        if (from.isAfter(to)) {
                            responseLabel.setText("Špatně zadané rozmezí");
                            return;
                        }
                        fromDate = from;
                        toDate = to;
                        appView.hidePopup();
                    }
                });
                break;
            default:
                setSelected(button);
                break;
        }
        if (!prevFromDate.isEqual(fromDate) && !prevToDate.isEqual(toDate)) {
            page.update();
        }
    }
}

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
    private ToggleButton oneMonthButton, threeMonthsButton, halfYearButton, oneYearButton, customRangeButton;

    public TimeSelectorView(Page page, LocalDate fromDate, LocalDate toDate) {
        super();
        this.page = page;
        this.fromDate = fromDate;
        this.toDate = toDate;
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
//        if (button.isSelected()) {
//            button.setSelected(true);
//            return;
//        }
        switch (button.getId()) {
            case "oneMonthButton":
                setSelected(oneMonthButton);
                fromDate = LocalDate.now().minusMonths(1);
                toDate = LocalDate.now();
                break;
            case "threeMonthsButton":
                setSelected(threeMonthsButton);
                fromDate = LocalDate.now().minusMonths(3);
                toDate = LocalDate.now();
                break;
            case "halfYearButton":
                setSelected(halfYearButton);
                fromDate = LocalDate.now().minusMonths(6);
                toDate = LocalDate.now();
                break;
            case "oneYearButton":
                setSelected(oneYearButton);
                fromDate = LocalDate.now().minusYears(1);
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
        //TODO
        if ((prevFromDate == null && prevToDate == null) || !prevFromDate.isEqual(fromDate) || !prevToDate.isEqual(toDate)) {
            page.update();
        }
    }
}

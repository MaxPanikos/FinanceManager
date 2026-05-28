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
    private ToggleButton threeMonthsButton, halfYearButton, oneYearButton, wholeButton, customRangeButton;

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

    private void setButtonVisibility(ToggleButton button, boolean visible) {
        if (button != null) {
            button.setVisible(visible);
            button.setManaged(visible);
        }
    }

    public void setThreeMonthsButtonVisible(boolean visible) {
        setButtonVisibility(threeMonthsButton, visible);
    }

    public void setHalfYearButtonVisible(boolean visible) {
        setButtonVisibility(halfYearButton, visible);
    }

    public void setOneYearButtonVisible(boolean visible) {
        setButtonVisibility(oneYearButton, visible);
    }

    public void setWholeButtonVisible(boolean visible) {
        setButtonVisibility(wholeButton, visible);
    }

    public void setCustomRangeButtonVisible(boolean visible) {
        setButtonVisibility(customRangeButton, visible);
    }

    private void setSelected (ToggleButton button) {
        if (button.isSelected()) {
            button.setSelected(false);
        }
    }

    @FXML
    private void onButtonClicked(ActionEvent event) {
        LocalDate prevFromDate = fromDate;
        LocalDate prevToDate = toDate;
        ToggleButton button = (ToggleButton) event.getSource();
        switch (button.getId()) {
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
            case "wholeButton":
                setSelected(wholeButton);
                fromDate = page.getAppView().getProfile().getLedger().get(0).getDate().toLocalDate();
                toDate = page.getAppView().getProfile().getLedger().get(page.getAppView().getProfile().getLedger().getSize()-1).getDate().toLocalDate();
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
                        page.update();
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

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }
}

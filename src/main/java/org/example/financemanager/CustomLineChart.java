package org.example.financemanager;

import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CustomLineChart extends LineChart<String, Number> implements CustomChart {
    private LocalDate fromDate, toDate;
    private Ledger ledger;
    private DateTimeFormatter formatter;

    public CustomLineChart(Ledger ledger) {
        super(new CategoryAxis(), new NumberAxis());
        this.fromDate = LocalDate.now().minusYears(1);
        this.toDate = LocalDate.now();
        this.ledger = ledger;
        this.formatter = DateTimeFormatter.ofPattern("d.M. yyyy");
        setAnimated(false);
        update();
    }

    @Override
    public void update() {
        this.getData().clear();
        ArrayList<LocalDate> dates = getDates();

        if (dates.isEmpty()) {
            return;
        }

        XYChart.Series<String, Number> balanceSeries = new XYChart.Series<>();
        balanceSeries.setName("Stav účtu");

        layout();

        for (LocalDate date : dates) {
            String formatedDay = date.format(formatter);
            double balance = ledger.getFloorBalance(date);
            XYChart.Data<String, Number> data = new XYChart.Data<>(formatedDay, balance);
            balanceSeries.getData().add(data);
            data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                if (newNode != null) {
                    Tooltip tooltip = new Tooltip(formatedDay + ": " + balance + " " + ledger.getCurrency().getSymbol());
                    tooltip.setShowDelay(javafx.util.Duration.millis(100));
                    Tooltip.install(newNode, tooltip);
                }
            });
        }

        this.getData().add(balanceSeries);
    }

    /**
     * gets dates for chart (AI USED)
     * @return ArrayList of LocalDates
     */
    protected ArrayList<LocalDate> getDates() {
        return ledger.getTransactions().stream()
                .map(transaction -> transaction.getDate().toLocalDate())
                .filter(date -> {
                    boolean isAfterOrEqualFrom = (fromDate == null) || !date.isBefore(fromDate);
                    boolean isBeforeOrEqualTo = (toDate == null) || !date.isAfter(toDate);
                    return isAfterOrEqualFrom && isBeforeOrEqualTo;
                })
                .distinct()
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}

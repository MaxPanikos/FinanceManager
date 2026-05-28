package org.example.financemanager;

import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CustomLineChart extends LineChart<String, Number> implements CustomChart {
    private LocalDate fromDate, toDate;
    private Ledger ledger;
    private DateTimeFormatter formatter;

    public CustomLineChart(LocalDate fromDate, LocalDate toDate, Ledger ledger) {
        super(new CategoryAxis(), new NumberAxis());
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.ledger = ledger;
        this.formatter = DateTimeFormatter.ofPattern("d.M. yyyy");
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

        for (LocalDate date : dates) {
            String formatedDay = date.format(formatter);
            double balance = ledger.getFloorBalance(date);
            XYChart.Data<String, Number> data = new XYChart.Data<>(formatedDay, balance);
            balanceSeries.getData().add(data);
            data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                //AI
                if (newNode != null) {
                    Tooltip tooltip = new Tooltip(formatedDay + ": " + balance + " Kč");
                    tooltip.setShowDelay(javafx.util.Duration.millis(100));
                    Tooltip.install(newNode, tooltip);
                }
            });
        }

        this.getData().add(balanceSeries);
    }

    private ArrayList<LocalDate> getDates() {
        ArrayList<LocalDate> dates = new ArrayList<>();
        ledger.getTransactions().forEach(transaction -> {
            dates.add(transaction.getDate().toLocalDate());
        });
        return dates;
    }
}

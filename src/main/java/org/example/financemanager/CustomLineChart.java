package org.example.financemanager;

import javafx.scene.chart.*;

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
        this.formatter = DateTimeFormatter.ofPattern("d.M.");
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

            balanceSeries.getData().add(new XYChart.Data<>(formatedDay, balance));
        }

        this.getData().add(balanceSeries);
    }

    private ArrayList<LocalDate> getDates() {
        ArrayList<LocalDate> dates = new ArrayList<>();

        LocalDate start = fromDate;
        LocalDate end = toDate;

        if (start == null || end == null) {
            if (ledger.getTransactions() == null || ledger.getTransactions().isEmpty()) {
                return dates;
            }
            start = ledger.getTransactions().getFirst().getDate().toLocalDate();
            end = LocalDate.now();
        }

        Period difference = Period.between(start, end);
        int years = difference.getYears();
        int oneInMonths;

        if (years < 2) {
            oneInMonths = 3;
        } else if (years < 5) {
            oneInMonths = 6;
        } else {
            oneInMonths = 12;
        }
        LocalDate actual = start;
        while (!actual.isAfter(end)) {
            dates.add(actual);
            actual = actual.plusMonths(oneInMonths);
        }
        if (!dates.contains(end)) {
            dates.add(end);
        }

        return dates;
    }
}

package org.example.financemanager;

import javafx.scene.chart.Chart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CustomPieChart extends PieChart implements CustomChart {
    private LocalDate fromDate, toDate;
    private Ledger ledger;
    private String category;

    public CustomPieChart(LocalDate startDate, LocalDate endDate, Ledger ledger, String category) {
        super();
        this.fromDate = startDate;
        this.toDate = endDate;
        this.ledger = ledger;
        this.category = category;
        update();
    }

    @Override
    public void update() {
        this.getData().clear();
        ArrayList<Transaction> transactions;
        if (fromDate == null || toDate == null) {
            transactions = ledger.getTransactions();
        } else {
            transactions = ledger.getTransactionsInRange(fromDate.atStartOfDay(), toDate.atStartOfDay());
        }

        HashMap<TransactionTypes, PieChart.Data> dataMap = new HashMap<>();
        for (TransactionTypes type : TransactionTypes.values()) {
            if (type.getType().equals(category)) {
                dataMap.put(type, new PieChart.Data(type.getLabel(), 0.0));
            }
        }

        for (Transaction transaction : transactions) {
            TransactionTypes type = transaction.getType();
            if (dataMap.containsKey(type)) {
                double currentAmount = dataMap.get(type).getPieValue();
                dataMap.get(type).setPieValue(currentAmount + transaction.getAmount());
            }
        }

        this.getData().clear();
        for (PieChart.Data data : dataMap.values()) {
            if (data.getPieValue() > 0.0) {
                this.getData().add(data);
                data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                    if (newNode != null) {
                        Tooltip tooltip = new Tooltip(data.getPieValue() + " " + ledger.getCurrency().getSymbol());
                        tooltip.setShowDelay(javafx.util.Duration.millis(100));
                        Tooltip.install(newNode, tooltip);
                    }
                });
            }
        }
    }
}

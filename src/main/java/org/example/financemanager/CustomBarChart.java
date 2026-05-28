package org.example.financemanager;

import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

public class CustomBarChart extends BarChart<String, Number> implements CustomChart{
    private LocalDate fromDate, toDate;
    private Ledger ledger;

    public CustomBarChart(Ledger ledger) {
        super(new CategoryAxis(), new NumberAxis());
        this.fromDate = LocalDate.now().minusYears(1);
        this.toDate = LocalDate.now();
        this.ledger = ledger;
        setAnimated(false);
        update();
    }

    @Override
    public void update() {
        this.getData().clear();
        ArrayList<Transaction> transactions;
        if (fromDate == null || toDate == null) {
            transactions = ledger.getTransactionsInRange(LocalDate.now().atStartOfDay().minusYears(1), LocalDate.now().atStartOfDay());
        } else {
            transactions = ledger.getTransactionsInRange(fromDate.atStartOfDay(), toDate.atStartOfDay());
        }
        LinkedHashMap<YearMonth, Double> incomeData = new LinkedHashMap<>();
        LinkedHashMap<YearMonth, Double> expenseData = new LinkedHashMap<>();

        YearMonth currentMonth = YearMonth.from(fromDate);
        YearMonth endMonth = YearMonth.from(toDate);

        while (!currentMonth.isAfter(endMonth)) {
            incomeData.put(currentMonth, 0.0);
            expenseData.put(currentMonth, 0.0);
            currentMonth = currentMonth.plusMonths(1);
        }

        for (Transaction transaction : transactions) {
            YearMonth month = YearMonth.from(transaction.getDate());
            if (transaction.getType().getType().equals("Příjem")) {
                if (!incomeData.containsKey(month)) {
                    incomeData.put(month, transaction.getAmount());
                } else {
                    incomeData.put(month, incomeData.get(month) + transaction.getAmount());
                }
            } else {
                if (!expenseData.containsKey(month)) {
                    expenseData.put(month, transaction.getAmount() * -1);
                } else {
                    expenseData.put(month, expenseData.get(month) + (transaction.getAmount() * -1));
                }
            }
        }

        layout();

        XYChart.Series<String, Number> income = new XYChart.Series<>();
        income.setName("Příjmy");
        for (Map.Entry<YearMonth, Double> entry : incomeData.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("cs", "CZ")), entry.getValue());
            income.getData().add(data);
            data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                if (newNode != null) {
                    Tooltip tooltip = new Tooltip(entry.getValue() + " " + ledger.getCurrency().getSymbol());
                    tooltip.setShowDelay(javafx.util.Duration.millis(100));
                    Tooltip.install(newNode, tooltip);
                }
            });
        }

        XYChart.Series<String, Number> expense = new XYChart.Series<>();
        expense.setName("Výdaje");
        for (Map.Entry<YearMonth, Double> entry : expenseData.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("cs", "CZ")), entry.getValue());
            expense.getData().add(data);
            data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                if (newNode != null) {
                    Tooltip tooltip = new Tooltip((entry.getValue() * -1) + " " + ledger.getCurrency().getSymbol());
                    tooltip.setShowDelay(javafx.util.Duration.millis(100));
                    Tooltip.install(newNode, tooltip);
                }
            });
        }
        this.getData().addAll(income, expense);
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

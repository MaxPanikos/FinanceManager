package org.example.financemanager;

import javafx.scene.chart.*;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

public class CustomBarChart extends BarChart<String, Number> implements CustomChart{
    private LocalDate fromDate, toDate;
    private Ledger ledger;

    public CustomBarChart(LocalDate fromDate, LocalDate toDate, Ledger ledger) {
        super(new CategoryAxis(), new NumberAxis());
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.ledger = ledger;
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
        LinkedHashMap<Month, Double> incomeData = new LinkedHashMap<>();
        LinkedHashMap<Month, Double> expenseData = new LinkedHashMap<>();
        for (Transaction transaction : transactions) {
            Month month = transaction.getDate().toLocalDate().getMonth();
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
        XYChart.Series<String, Number> income = new XYChart.Series<>();
        income.setName("Příjmy");
        for (Map.Entry<Month, Double> entry : incomeData.entrySet()) {
            income.getData().add(new XYChart.Data<>(entry.getKey().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("cs", "CZ")), entry.getValue()));
        }

        XYChart.Series<String, Number> expense = new XYChart.Series<>();
        expense.setName("Výdaje");
        for (Map.Entry<Month, Double> entry : expenseData.entrySet()) {
            expense.getData().add(new XYChart.Data<>(entry.getKey().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("cs", "CZ")), entry.getValue()));
        }

        this.getData().addAll(income, expense);
    }
}

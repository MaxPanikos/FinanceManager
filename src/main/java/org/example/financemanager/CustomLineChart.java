package org.example.financemanager;

import javafx.scene.chart.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class CustomLineChart extends LineChart implements CustomChart {
    private LocalDate fromDate, toDate;
    private Ledger ledger;
    public CustomLineChart(LocalDate fromDate, LocalDate toDate, Ledger ledger) {
        super(new CategoryAxis(), new NumberAxis());
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.ledger = ledger;
    }

    @Override
    public void update() {
        //TODO
        this.getData().clear();
        ArrayList<Transaction> transactions;
        if (fromDate == null || toDate == null) {
            transactions = ledger.getTransactions();
        } else {
            transactions = ledger.getTransactionsInRange(fromDate.atStartOfDay(), toDate.atStartOfDay());
        }
        XYChart.Series<String, Number> balance = new XYChart.Series<>();
        balance.setName("Stav účtu");
        balance.getData().add(new XYChart.Data<>("1.1.", 50000));
        balance.getData().add(new XYChart.Data<>("15.1.", 62000));
        balance.getData().add(new XYChart.Data<>("1.2.", 58000));
        balance.getData().add(new XYChart.Data<>("15.2.", 71000));

        this.getData().add(balance);
    }
}

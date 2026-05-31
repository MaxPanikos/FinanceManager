package org.example.financemanager;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;

public class HomepageView extends Page {
    private DecimalFormat df;
    private Currency currency;

    @FXML
    private FlowPane flowPane;

    @FXML
    public void initialize () {
        flowPane.getChildren().add(balanceTile());
    }

    @Override
    public void update() {
        //TODO
    }

    public HomepageView(AppView appView) {
        super(appView);
        this.df = appView.getFormat();
        this.currency = appView.getProfile().getLedger().getCurrency();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private VBox balanceTile () {
        LocalDateTime to = LocalDate.now().atStartOfDay();
        LocalDateTime from = to.withDayOfMonth(1);
        ArrayList<Transaction> transactions = appView.getProfile().getLedger().getTransactionsInRange(from, to);
        double income = 0;
        double expense = 0;
        for (Transaction tx : transactions) {
            if (tx.getAmount() > 0) {
                income += tx.getAmount();
            } else {
                expense += tx.getAmount()*-1;
            }
        }
        double remaining = income - expense;

        VBox vBox = new VBox();
        Label incomeLabel = new Label("Příjem tento měsíc: " + df.format(income) + " " + currency.getSymbol());
        Label expenseLabel = new Label("Výdaje tento měsíc: " + df.format(expense) + " " + currency.getSymbol());
        Label remainingLabel = new Label("Zbývá: " + df.format(remaining) + " " + currency.getSymbol());
        vBox.getChildren().addAll(incomeLabel, expenseLabel, remainingLabel);
        return vBox;
    }

    private VBox quickAddTx () {
        VBox vBox = new VBox();
        return vBox;
    }
}

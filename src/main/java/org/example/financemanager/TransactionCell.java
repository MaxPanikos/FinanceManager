package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.time.format.DateTimeFormatter;

public class TransactionCell extends GridPane {
    private int index;
    private Transaction transaction;
    private Ledger ledger;
    @FXML
    private Label indexLabel, typeLabel, amountLabel, dateLabel;
    @FXML
    private Button moreButton;

    @FXML
    public void initialize() {
        indexLabel.setText(index + "");
        typeLabel.setText(transaction.getType().getLabel());
        amountLabel.setText(transaction.getAmount() + " " + ledger.getCurrency().getSymbol());
        dateLabel.setText(transaction.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        if (transaction.getAmount() < 0) {
            this.setStyle("-fx-background-color: rgba(255,0,0,0.1)");
        } else {
            this.setStyle("-fx-background-color: rgba(97,223,104,0.1)");
        }
    }

    public TransactionCell(Transaction transaction, Ledger ledger, int index) {
        this.transaction = transaction;
        this.index = index;
        this.ledger = ledger;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("transaction-cell.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

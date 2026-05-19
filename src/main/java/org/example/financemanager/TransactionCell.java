package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class TransactionCell extends GridPane {
    private int index;
    private Transaction transaction;
    @FXML
    private Label indexLabel, typeLabel, amountLabel, dateLabel;
    @FXML
    private Button moreButton;

    @FXML
    public void initialize() {
        indexLabel.setText(index + "");
        typeLabel.setText(transaction.getType().getLabel());
        amountLabel.setText(transaction.getAmount() + "");
        dateLabel.setText(transaction.getDate().toString());
    }

    public TransactionCell(Transaction transaction, int index) {
        this.transaction = transaction;
        this.index = index;
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

package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;

public class TransactionView extends StackPane {
    private Ledger ledger;
    @FXML
    private Label rangeLabel, noTxLabel;
    @FXML
    private VBox contentBox, txList;

    @FXML
    public void initialize() {
        if (ledger.getSize() == 0) {
            contentBox.setVisible(false);
            noTxLabel.setVisible(true);
        } else {
            Transaction firstTx = ledger.get(0);
            Transaction lastTx = ledger.get(ledger.getSize() - 1);
            String firstDate = firstTx.getDate().toLocalDate().format(DateTimeFormatter.ofPattern("d. M. yyyy"));
            String lastDate = lastTx.getDate().toLocalDate().format(DateTimeFormatter.ofPattern("d. M. yyyy"));
            rangeLabel.setText(firstDate + " - " + lastDate);
        }

        int i = 0;
        for (Transaction tx : ledger.getTransactions()) {
            TransactionCell cell = new TransactionCell(tx, i);
            txList.getChildren().add(cell);
            i++;
        }
    }

    public TransactionView(Ledger ledger) {
        this.ledger = ledger;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("transaction-view.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

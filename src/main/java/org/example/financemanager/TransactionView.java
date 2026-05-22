package org.example.financemanager;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.text.PlainDocument;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TransactionView extends Page {
    private Ledger ledger;

    private LocalDate fromDate;
    private LocalDate toDate;

    @FXML
    private Label rangeLabel, noTxLabel;
    @FXML
    private VBox contentBox, txList;
    @FXML
    private Button rangeButton;

    @FXML
    public void initialize() {
        if (ledger.getSize() == 0) {
            contentBox.setVisible(false);
            noTxLabel.setVisible(true);
        } else {
            fromDate = ledger.get(0).getDate().toLocalDate();
            toDate = ledger.get(ledger.getSize() - 1).getDate().toLocalDate();
            setPage();
        }
    }

    public TransactionView(AppView appView, Ledger ledger) {
        super(appView);
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

    private void setPage () {
        String firstDate = fromDate.format(DateTimeFormatter.ofPattern("d. M. yyyy"));
        String lastDate = toDate.format(DateTimeFormatter.ofPattern("d. M. yyyy"));
        rangeLabel.setText(firstDate + " - " + lastDate);

        txList.getChildren().clear();
        ArrayList<Transaction> transactions = ledger.getTransactionsInRange(fromDate.atStartOfDay(), toDate.atStartOfDay());
        int i = 1;
        for (Transaction tx : transactions) {
            TransactionCell cell = new TransactionCell(tx, ledger, i);
            txList.getChildren().add(cell);
            i++;
        }
    }

    protected void setRange (LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return;
        }
        this.fromDate = start;
        this.toDate = end;
        Platform.runLater(() -> setPage());
    }

    @FXML
    private void rangePicker () {
        appView.showPopup(new DateRangePopup(appView, this));
    }

    @Override
    public void update() {
        setPage();
    }
}

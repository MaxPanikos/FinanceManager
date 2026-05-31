package org.example.financemanager;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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

    public TransactionView(AppView appView) {
        super(appView);
        this.ledger = appView.getProfile().getLedger();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("transaction-view.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * loads page content
     */
    private void setPage () {
        if (ledger.getSize() == 0) {
            contentBox.setVisible(false);
            noTxLabel.setVisible(true);
            return;
        } else {
            contentBox.setVisible(true);
            noTxLabel.setVisible(false);
        }
        if (fromDate == null || toDate == null) {
            return;
        }
        String firstDate = fromDate.format(DateTimeFormatter.ofPattern("d. M. yyyy"));
        String lastDate = toDate.format(DateTimeFormatter.ofPattern("d. M. yyyy"));
        rangeLabel.setText(firstDate + " - " + lastDate);
        txList.getChildren().clear();
        ArrayList<Transaction> transactions = ledger.getTransactionsInRange(fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX));
        int i = 0;
        for (Transaction tx : transactions) {
            TransactionCell cell = new TransactionCell(tx, this, ledger, i);
            txList.getChildren().add(cell);
            i++;
        }
    }

    /**
     * sets range in which transactions are displayed
     * @param start
     * @param end
     */
    protected void setRange (LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return;
        }
        this.fromDate = start;
        this.toDate = end;
        Platform.runLater(() -> setPage());
    }

    /**
     * shows range picker popup
     */
    @FXML
    private void rangePicker () {
        appView.showPopup(new DateRangePopup(appView){
            @Override
            public void save (DatePicker fromPicker, DatePicker toPicker, Label responseLabel) {
                LocalDate from = fromPicker.getValue();
                LocalDate to = toPicker.getValue();
                if (from.isAfter(to)) {
                    responseLabel.setText("Špatně zadané rozmezí");
                    return;
                }
                setRange(from, to);
                appView.hidePopup();
            }
        });
    }

    @Override
    public void update() {
        setPage();
    }
}

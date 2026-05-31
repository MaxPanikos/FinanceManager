package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class TransactionCell extends GridPane {
    private int index;
    private Transaction transaction;
    private Ledger ledger;
    private Page page;

    private ContextMenu popup;

    @FXML
    private Label indexLabel, typeLabel, amountLabel, dateLabel;
    @FXML
    private Button moreButton;

    @FXML
    public void initialize() {
        indexLabel.setText(index+1 + "");
        typeLabel.setText(transaction.getType().getLabel());
        amountLabel.setText(transaction.getAmount() + " " + ledger.getCurrency().getSymbol());
        dateLabel.setText(transaction.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        if (transaction.getAmount() < 0) {
            this.setStyle("-fx-background-color: rgba(255,0,0,0.1)");
        } else {
            this.setStyle("-fx-background-color: rgba(97,223,104,0.1)");
        }
    }

    public TransactionCell(Transaction transaction, Page page, Ledger ledger, int index) {
        this.transaction = transaction;
        this.index = index;
        this.ledger = ledger;
        this.page = page;
        this.popup = new ContextMenu();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("transaction-cell.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * shows popup
     */
    @FXML
    private void clickedOnMore () {
        if (popup.isShowing()) {
            popup.hide();
        }
        popup.getStyleClass().add("popup");
        popup.getItems().clear();
        MenuItem button = new MenuItem("Smazat");
        button.setOnAction(event -> {
            deleteTransaction();
            popup.hide();
        });
        popup.getItems().add(button);
        popup.show(moreButton, Side.TOP, 0, 0);
    }

    /**
     * removes transaction
     */
    private void deleteTransaction() {
        ledger.remove(index);
        page.getAppView().update();
    }
}

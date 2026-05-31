package org.example.financemanager;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class HomepageView extends Page {
    private DecimalFormat df;
    private Currency currency;
    private String currentMonth;

    @FXML
    private FlowPane flowPane;
    @FXML
    private VBox balanceTile;
    @FXML
    private Label incomeLabel, expenseLabel, remainingLabel, addTxResponseLabel;
    @FXML
    private Spinner<Double> spinner;
    @FXML
    private ComboBox<TransactionTypes> comboBox;

    @FXML
    public void initialize () {
        updateCategory(false);
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(-1_000_000_000.0, 1_000_000_000, 0.0, 100.0);
        spinner.setValueFactory(valueFactory);
        spinner.setEditable(true);
        TextField editor = spinner.getEditor();
        editor.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([0-9]*[\\.,]?[0-9]*)")) {
                if (editor.getText().startsWith("-")) {
                    updateCategory(true);
                } else {
                    updateCategory(false);
                }
                return change;
            }
            return null;
        }));
        editor.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.contains(",")) {
                editor.setText(newVal.replace(",", "."));
            }
        });

        setBalanceTile();
    }

    @Override
    public void update() {
        //TODO
    }

    public HomepageView(AppView appView) {
        super(appView);
        this.df = appView.getFormat();
        this.currency = appView.getProfile().getLedger().getCurrency();
        this.currentMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("cs-CZ"));
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void setBalanceTile () {
        LocalDateTime to = LocalDate.now().atStartOfDay();
        LocalDateTime from = to.withDayOfMonth(1);
        to = LocalDate.now().atTime(LocalTime.MAX);
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

        incomeLabel.setText("Příjem za " + currentMonth + ": " + df.format(income) + " " + currency.getSymbol());
        expenseLabel.setText("Výdaje za " + currentMonth + ": " + df.format(expense) + " " + currency.getSymbol());
        remainingLabel.setText("Za " + currentMonth + " zůstalo: " + df.format(remaining) + " " + currency.getSymbol());
    }
    private void updateCategory (boolean isExpense) {
        String type = isExpense ? "Výdaj" : "Příjem";
        List<TransactionTypes> filtred = Arrays.stream(TransactionTypes.values()).filter(t -> t.getType().equals(type)).collect(Collectors.toList());
        comboBox.setItems(FXCollections.observableArrayList(filtred));
        if (!filtred.isEmpty()) {
            comboBox.getSelectionModel().selectFirst();
        }
    }
    @FXML
    private void addTx () {
        Double amount = spinner.getValue();
        TransactionTypes type = comboBox.getValue();
        if (amount == null || type == null) {
            addTxResponseLabel.setText("Vyplňte všechna pole");
            return;
        }
        if (amount == 0) {
            addTxResponseLabel.setText("Nastavte nějakou částku");
            return;
        }
        try {
            Transaction transaction = new Transaction(amount, type, LocalDateTime.now());
            appView.getProfile().getLedger().add(transaction);
            spinner.getValueFactory().setValue(0.0);
            comboBox.getSelectionModel().selectFirst();
            appView.update();
        } catch (Exception e) {
            addTxResponseLabel.setText("Nastala neočekávaná chyba");
        }
    }
}

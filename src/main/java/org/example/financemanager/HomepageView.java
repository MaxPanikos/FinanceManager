package org.example.financemanager;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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

    private Text incomeText2, expenseText2, remainingText2;

    @FXML
    private FlowPane flowPane;
    @FXML
    private TextFlow incomeTextFlow, expanseTextFlow, remainingTextFlow;
    @FXML
    private Label addTxResponseLabel;
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
        incomeText2.setText(df.format(income));
        expenseText2.setText(df.format(expense));
        remainingText2.setText(df.format(remaining));
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

        String defaultStyle = "-fx-font-size: 16; -fx-fill: whitesmoke; -fx-font-weight: bold";

        Text incomeText1 = new Text("Příjem za " + currentMonth + ": ");
        incomeText1.setStyle(defaultStyle);
        incomeText2 = new Text(df.format(income));
        incomeText2.setStyle("-fx-font-size: 16; -fx-fill: #5cbc5c; -fx-font-weight: bold");
        Text incomeText3 = new Text(" " + currency.getSymbol());
        incomeText3.setStyle(defaultStyle);
        incomeTextFlow.getChildren().addAll(incomeText1, incomeText2, incomeText3);

        Text expanseText1 = new Text("Výdaje za " + currentMonth + ": ");
        expanseText1.setStyle(defaultStyle);
        expenseText2 = new Text(df.format(expense));
        expenseText2.setStyle("-fx-font-size: 16; -fx-fill: #bf6666; -fx-font-weight: bold");
        Text expanseText3 = new Text(" " + currency.getSymbol());
        expanseText3.setStyle(defaultStyle);
        expanseTextFlow.getChildren().addAll(expanseText1, expenseText2, expanseText3);

        Text remainingText1 = new Text("Za " + currentMonth + " zůstalo: ");
        remainingText1.setStyle(defaultStyle);
        remainingText2 = new Text(df.format(remaining));
        if (remaining >= 0) {
            remainingText2.setStyle("-fx-font-size: 16; -fx-fill: #5cbc5c; -fx-font-weight: bold");
        } else {
            remainingText2.setStyle("-fx-font-size: 16; -fx-fill: #bf6666; -fx-font-weight: bold");
        }
        Text remainingText3 = new Text(" " + currency.getSymbol());
        remainingText3.setStyle(defaultStyle);
        remainingTextFlow.getChildren().addAll(remainingText1, remainingText2, remainingText3);
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

package org.example.financemanager;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class HomepageView extends Page {
    private DecimalFormat df;
    private Currency currency;
    private String currentMonth;

    private Text incomeText2, expenseText2, remainingText2;
    private CustomLineChart chart;
    private DateTimeFormatter formatter;

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
    private VBox chartBox, txList;

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
        setChartTile();
        setTxList();
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

        chart.update();
    }

    public HomepageView(AppView appView) {
        super(appView);
        this.df = appView.getFormat();
        this.currency = appView.getProfile().getLedger().getCurrency();
        this.currentMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("cs-CZ"));
        this.formatter = DateTimeFormatter.ofPattern("dd. MM. yyyy");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     *sets balance tile
     */
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

    /**
     * updates transaction category
     * @param isExpense
     */
    private void updateCategory (boolean isExpense) {
        String type = isExpense ? "Výdaj" : "Příjem";
        List<TransactionTypes> filtred = Arrays.stream(TransactionTypes.values()).filter(t -> t.getType().equals(type)).collect(Collectors.toList());
        comboBox.setItems(FXCollections.observableArrayList(filtred));
        if (!filtred.isEmpty()) {
            comboBox.getSelectionModel().selectFirst();
        }
    }

    /**
     * checks user inputs and add transaction
     */
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
            addTxResponseLabel.setText("");
        } catch (Exception e) {
            addTxResponseLabel.setText("Nastala neočekávaná chyba");
        }
    }

    /**
     * sets chart tile
     */
    private void setChartTile () {
        chart = new CustomLineChart(appView.getProfile().getLedger()){
            @Override
            protected ArrayList<LocalDate> getDates () {
                ArrayList<LocalDate> dates = new ArrayList<>();
                LocalDate dnes = LocalDate.now();
                LocalDate predMesicem = dnes.minusMonths(1);
                for (LocalDate datum = predMesicem; !datum.isAfter(dnes); datum = datum.plusDays(5)) {
                    dates.add(datum);
                }
                if (!dates.get(dates.size() - 1).equals(dnes)) {
                    dates.add(dnes);
                }
                return dates;
            }
        };
        chart.setPrefSize(400, 250);
        chartBox.getChildren().add(chart);
    }

    /**
     * sets transaction list
     */
    private void setTxList () {
        Ledger ledger = appView.getProfile().getLedger();
        ArrayList<Transaction> lastTen = new ArrayList<>();
        if (ledger.getSize() == 0) {
            return;
        }
        for (int i = ledger.getSize()-1; i > ledger.getSize()-11; i--) {
            lastTen.add(ledger.get(i));
            if (i == 0) {
                break;
            }
        }
        for (Transaction tx : lastTen) {
            Label amountLabel = new Label(df.format(tx.getAmount()) + " " + appView.getProfile().getLedger().getCurrency().getSymbol());
            amountLabel.setPrefWidth(150);
            amountLabel.getStyleClass().add("description");

            Label typeLabel = new Label(tx.getType().getLabel());
            typeLabel.setPrefWidth(120);
            typeLabel.getStyleClass().add("description");

            Label dateLabel = new Label(tx.getDate().format(formatter));
            dateLabel.getStyleClass().add("description");

            HBox row = new HBox(15, amountLabel, typeLabel, dateLabel);
            row.setPadding(new Insets(3, 5, 3, 5));
            if (tx.getAmount() > 0) {
                row.setStyle("-fx-background-color: rgba(92,159,92,0.3); -fx-border-color: #c3c3c3; -fx-border-width: 0 0 1 0; -fx-background-radius: 5; -fx-border-radius: 5");
            } else {
                row.setStyle("-fx-background-color: rgba(128,71,71,0.3); -fx-border-color: #c3c3c3; -fx-border-width: 0 0 1 0; -fx-background-radius: 5; -fx-border-radius: 5");
            }
            txList.getChildren().add(row);
        }
    }
}

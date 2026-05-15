package org.example.financemanager;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.controlsfx.control.ToggleSwitch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AddTransactionView extends VBox {
    private AppView appView;
    private boolean userChoice;

    @FXML
    private Spinner<Double> amountSpinner;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ToggleSwitch typeSwitch;
    @FXML
    private ComboBox<TransactionTypes> typeComboBox;
    @FXML
    private Label responseLabel;

    @FXML
    public void initialize () {
        datePicker.setValue(LocalDate.now());

        amountSpinner.setEditable(true);
        SpinnerValueFactory<Double> factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(-1_000_000_000.0, 1_000_000_000.0, 0.0, 10.0);
        amountSpinner.setValueFactory(factory);

        TextField editor = amountSpinner.getEditor();
        editor.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([0-9]*[\\.,]?[0-9]*)")) {
                if (editor.getText().startsWith("-")) {
                    Platform.runLater(() -> {
                        typeSwitch.setSelected(true);
                        typeSwitch.setDisable(true);
                        this.userChoice = false;
                    });
                } else {
                    if (!userChoice) {
                        Platform.runLater(() -> {
                            typeSwitch.setSelected(false);
                            typeSwitch.setDisable(false);
                        });
                        this.userChoice = true;
                    }
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


        typeComboBox.setConverter(new StringConverter<TransactionTypes>() {
            @Override
            public String toString(TransactionTypes t) { return (t == null) ? "" : t.getLabel(); }
            @Override
            public TransactionTypes fromString(String s) { return null; }
        });

        typeSwitch.selectedProperty().addListener((obs, oldVal, newVal) -> {
            updateCategory(newVal);
        });
        updateCategory(false);
    }
    public AddTransactionView(AppView appView) {
        super();
        this.appView = appView;
        this.userChoice = true;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-transaction-view.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //AI
    private void updateCategory (boolean isExpense) {
        String type = isExpense ? "Výdaj" : "Příjem";
        var filtred = Arrays.stream(TransactionTypes.values()).filter(t -> t.getType().equals(type)).collect(Collectors.toList());
        typeComboBox.setItems(FXCollections.observableArrayList(filtred));
        if (!filtred.isEmpty()) {
            typeComboBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    protected void closePopup () {
        appView.hidePopup();
    }

    @FXML
    protected void addTx () {
        try {
            double amount = amountSpinner.getValue();
            if (amount >= 0.0 && typeSwitch.isSelected()) {
                amount = amount * -1;
            }
            LocalDate date = datePicker.getValue();
            TransactionTypes type = typeComboBox.getValue();

            if (date != null) {
                responseLabel.setText("Prosim vyberte datum");
            }

            LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MIDNIGHT);
            Transaction tx = new Transaction(amount, type, dateTime);
            appView.getProfile().getLedger().add(tx);
            closePopup();
        } catch (Exception e) {
            responseLabel.setText("Nastala neocekavana chyba");
            System.err.println(e.getMessage());
        }
        System.out.println(appView.getProfile().getLedger()); //debug
        System.out.println(appView.getProfile().getLedger().getBalance());
    }
}

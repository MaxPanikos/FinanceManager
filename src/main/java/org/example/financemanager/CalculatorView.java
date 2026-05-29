package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.UnaryOperator;

public class CalculatorView extends Page{
    @FXML
    private TextField interestInitialDeposit, interestMonthlyDeposit, interestRate, interestMonths, inflationDeposit, inflationRate, inflationYears, loanLoan, loanRate, loanLength;
    @FXML
    private Button interestButton, inflationButton, loanButton;
    @FXML
    private Label interestResponse, inflationResponse, loanResponse, interestResult;

    @FXML
    private void initialize() {
        positiveDoubleTextField(interestInitialDeposit);
        positiveDoubleTextField(interestMonthlyDeposit);
        positiveDoubleTextField(interestRate);
        positiveIntTextField(interestMonths);
    }

    public CalculatorView(AppView appView) {
        super(appView);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("calculator-view.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        //nothing
    }

    @FXML
    private void calculateCompoundInterest () {
        if (interestInitialDeposit.getText().isBlank() || interestMonthlyDeposit.getText().isBlank() || interestRate.getText().isBlank() || interestMonths.getText().isBlank()) {
            interestResponse.setText("Prosím doplňte všechna textová pole");
            return;
        }

        double initialDeposit = Double.parseDouble(interestInitialDeposit.getText());
        double initialMonthlyDeposit = Double.parseDouble(interestMonthlyDeposit.getText());
        double rate = Double.parseDouble(interestRate.getText());
        int months = Integer.parseInt(interestMonths.getText());
        double result = coupoundInterest(initialDeposit, initialMonthlyDeposit, rate, months);
        interestResult.setText(String.valueOf(result));
    }

    private double coupoundInterest (double initialDeposit, double monthlyDeposit, double interestRate, int months) {
        if (interestRate == 0) {
            return initialDeposit + (monthlyDeposit * months);
        }
        double futureInitialDepositValue = initialDeposit * Math.pow(1 + interestRate, months);
        double futureMonthlyDepositValue = monthlyDeposit * ((Math.pow(1 + interestRate, months)) - 1) / interestRate;
        return futureInitialDepositValue + futureMonthlyDepositValue;
    }

    private double monthlyLoanPayment (double loan, double interestRate, int months) {
        return loan * ((Math.pow(1 + interestRate, months))/(Math.pow(1 + interestRate, months)-1));
    }

    private double inflation (double current, double yearlyInterestRate, int years) {
        return current/Math.pow(1 + yearlyInterestRate, years);
    }

    //AI
    public void positiveDoubleTextField (TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.getText().contains(",")) {
                change.setText(change.getText().replace(',', '.'));
            }
            String newText = change.getControlNewText();
            if (newText.matches("\\d*([.]\\d*)?")) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }
    public void positiveIntTextField(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }
}

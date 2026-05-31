package org.example.financemanager;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.function.UnaryOperator;

public class CalculatorView extends Page{
    private String currencySymbol;
    private DecimalFormat df;

    @FXML
    private TextField interestInitialDeposit, interestMonthlyDeposit, interestRate, interestMonths, inflationDeposit, inflationRate, inflationYears, loanLoan, loanRate, loanLength;
    @FXML
    private Button interestButton, inflationButton, loanButton;
    @FXML
    private Label interestResponse, inflationResponse, loanResponse, interestResult, inflationResult, loanResult, currencyLabel1, currencyLabel2, currencyLabel3, currencyLabel4;

    @FXML
    private void initialize() {
        positiveDoubleTextField(interestInitialDeposit, 1_000_000_000.0);
        positiveDoubleTextField(interestMonthlyDeposit, 10_000_000.0);
        positiveDoubleTextField(interestRate, 100.0);
        positiveIntTextField(interestMonths, 1200);

        positiveDoubleTextField(inflationDeposit, 1_000_000_000.0);
        positiveDoubleTextField(inflationRate, 1000.0);
        positiveIntTextField(inflationYears, 100);

        positiveDoubleTextField(loanLoan, 1_000_000_000.0);
        positiveDoubleTextField(loanRate, 100.0);
        positiveIntTextField(loanLength, 100);

        currencyLabel1.setText(currencySymbol);
        currencyLabel2.setText(currencySymbol);
        currencyLabel3.setText(currencySymbol);
        currencyLabel4.setText(currencySymbol);
    }

    public CalculatorView(AppView appView) {
        super(appView);
        this.currencySymbol = appView.getProfile().getLedger().getCurrency().getSymbol();
        this.df = appView.getFormat();
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

    /**
     * checks user inputs and calculates compound interest and displays it
     */
    @FXML
    private void calculateCompoundInterest () {
        if (interestInitialDeposit.getText().isBlank() || interestMonthlyDeposit.getText().isBlank() || interestRate.getText().isBlank() || interestMonths.getText().isBlank()) {
            interestResponse.setText("Prosím doplňte všechna textová pole");
            return;
        }

        try {
            double initialDeposit = Double.parseDouble(interestInitialDeposit.getText());
            double initialMonthlyDeposit = Double.parseDouble(interestMonthlyDeposit.getText());
            double rate = Double.parseDouble(interestRate.getText());
            int months = Integer.parseInt(interestMonths.getText());
            double result = coupoundInterest(initialDeposit, initialMonthlyDeposit, rate, months);
            interestResult.setText(df.format(result) + " " + currencySymbol);
        } catch (NumberFormatException e) {
            interestResponse.setText("Nastala neočekávaná chyba");
            interestResult.setText("???");
        }
    }

    /**
     * checks user inputs and calculates inflation leftover and displays it
     */
    @FXML
    private void calculateInflationLeftover () {
        if (inflationDeposit.getText().isBlank() || inflationRate.getText().isBlank() || inflationYears.getText().isBlank()) {
            inflationResponse.setText("Prosím doplňte všechna textová pole");
            return;
        }

        try {
            double initialDeposit = Double.parseDouble(inflationDeposit.getText());
            double rate = Double.parseDouble(inflationRate.getText());
            int years = Integer.parseInt(inflationYears.getText());
            double result = calculatePurchasingPower(initialDeposit, rate, years);
            inflationResult.setText(df.format(result) + " " + currencySymbol);
        } catch (NumberFormatException e) {
            inflationResponse.setText("Nastala neočekávaná chyba");
            inflationResult.setText("???");
        }
    }

    /**
     * checks user inputs and calculates monthly loan payment and displays it
     */
    @FXML
    private void calculateMonthlyLoanPayment () {
        if (loanLoan.getText().isBlank() || loanRate.getText().isBlank() || loanLength.getText().isBlank()) {
            loanResponse.setText("Prosím doplňte všechna textová pole");
            return;
        }

        try {
            double loan = Double.parseDouble(loanLoan.getText());
            double rate = Double.parseDouble(loanRate.getText());
            int years = Integer.parseInt(loanLength.getText());
            double result = monthlyLoanPayment(loan, rate, years*12);
            loanResult.setText(df.format(result) + " " + currencySymbol);
        } catch (NumberFormatException e) {
            loanResponse.setText("Nastala neočekávaná chyba");
            loanResult.setText("???");
        }
    }

    /**
     * calculates compound interest
     * @param initialDeposit
     * @param monthlyDeposit
     * @param annualInterestRate
     * @param months
     * @return
     */
    private double coupoundInterest (double initialDeposit, double monthlyDeposit, double annualInterestRate, int months) {
        double monthlyRate = (annualInterestRate / 100) / 12;
        if (monthlyRate == 0) {
            return initialDeposit + (monthlyDeposit * months);
        }
        double futureInitialDepositValue = initialDeposit * Math.pow(1 + monthlyRate, months);
        double futureMonthlyDepositValue = monthlyDeposit * ((Math.pow(1 + monthlyRate, months) - 1) / monthlyRate);
        return futureInitialDepositValue + futureMonthlyDepositValue;
    }

    private double monthlyLoanPayment (double loan, double interestRate, int months) {
        if (months == 0) {
            return loan;
        }
        double monthlyRate = (interestRate / 100) / 12;
        if (monthlyRate == 0) {
            return loan / months;
        }
        double numerator = monthlyRate * Math.pow(1 + monthlyRate, months);
        double denominator = Math.pow(1 + monthlyRate, months) - 1;
        return loan * (numerator / denominator);
    }

    /**
     * calculates purchasing power
     * @param current
     * @param yearlyInterestRate
     * @param years
     * @return
     */
    private double calculatePurchasingPower (double current, double yearlyInterestRate, int years) {
        double rate = yearlyInterestRate / 100;
        return current * Math.pow(1 - rate, years);
    }

    /**
     * sets available text field only for positive decimal numbers (AI USED)
     * @param textField text field you want to modify
     * @param maxValue maximal value that is allowed in that text field
     */
    public void positiveDoubleTextField(TextField textField, double maxValue) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.getText().contains(",")) {
                change.setText(change.getText().replace(',', '.'));
            }

            String newText = change.getControlNewText();
            if (newText.matches("\\d*([.]\\d*)?")) {
                if (newText.isEmpty() || newText.equals(".")) {
                    return change;
                }

                try {
                    double value = Double.parseDouble(newText);
                    if (value <= maxValue) {
                        return change;
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    /**
     * sets available text field only for positive whole numbers (AI USED)
     * @param textField text field you want to modify
     * @param maxValue maximal value that is allowed in that text field
     */
    public void positiveIntTextField(TextField textField, int maxValue) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("\\d*")) {
                if (newText.isEmpty()) {
                    return change;
                }

                try {
                    long value = Long.parseLong(newText);
                    if (value <= maxValue) {
                        return change;
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }
}

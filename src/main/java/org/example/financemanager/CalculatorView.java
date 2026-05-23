package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class CalculatorView extends Page{
    public CalculatorView(AppView appView) {
        super(appView);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("calculator-view.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update() {
        //nothing
    }

    private double coupoundInterest (double initialDeposit, double monthlyDeposit, double interestRate, int months) {
        double futureInitialDepositValue = initialDeposit * Math.pow(1 + interestRate, months);
        double futureMonthlyDepositValue = monthlyDeposit * ((Math.pow(1 + interestRate, months))-1)/months;
        return futureInitialDepositValue + futureMonthlyDepositValue;
    }

    private double monthlyLoanPayment (double loan, double interestRate, int months) {
        return loan * ((Math.pow(1 + interestRate, months))/(Math.pow(1 + interestRate, months)-1));
    }

    private double inflation (double current, double yearlyInterestRate, int years) {
        return current/Math.pow(1 + yearlyInterestRate, years);
    }
}

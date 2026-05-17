package org.example.financemanager;

import javafx.scene.shape.CubicCurveTo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Ledger implements Serializable {
    private ArrayList<Transaction> transactions;
    private double balance;
    private Currency currency;

    private final static long serialVersionUID = 1L;

    public Ledger (Currency currency) {
        this.transactions = new ArrayList<>();
        this.balance = 0;
        this.currency = currency;
    }

    public boolean addWithDuplicityCheck (Transaction transaction) {
        if (transaction.getType().getType().equals("Výdaj") && transaction.getAmount() >= 0.0) {
            transaction.setAmount(transaction.getAmount() * -1);
        }
        int index = Collections.binarySearch(transactions, transaction);
        if (index >= 0) {
            if (isDuplicate(index, transaction)) {
                return false;
            }
            transactions.add(index, transaction);
            balance += transaction.getAmount();
            return true;
        } else {
            transactions.add(-index - 1, transaction);
            balance += transaction.getAmount();
            return true;
        }
    }

    public boolean add (Transaction transaction) {
        try {
            if (transaction.getType().getType().equals("Výdaj") && transaction.getAmount() >= 0.0) {
                transaction.setAmount(transaction.getAmount() * -1);
            }
            int index = Collections.binarySearch(transactions, transaction);
            if (index >= 0) {
                transactions.add(index, transaction);
                balance += transaction.getAmount();
                return true;
            } else {
                transactions.add(-index - 1, transaction);
                balance += transaction.getAmount();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<Transaction> getTransactionsInRange (LocalDateTime start, LocalDateTime end) {
        if (transactions.isEmpty()) {
            return new ArrayList<>();
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException();
        }
        Transaction startTx = new Transaction(0.0, TransactionTypes.OTHER_INCOME, start);
        int startingIndex = Collections.binarySearch(transactions, startTx);
        if (startingIndex < 0) {
            startingIndex = -startingIndex - 1;
        } else {
            while (startingIndex > 0 && transactions.get(startingIndex - 1).getDate().isEqual(start)) {
                startingIndex--;
            }
        }

        ArrayList<Transaction> inRangeTransactions = new ArrayList<>();
        for (int i = startingIndex; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            if (transaction.getDate().isAfter(end)) {
                break;
            }
            inRangeTransactions.add(transaction);
        }
        return inRangeTransactions;
    }

    public Transaction get (int index) {
        return transactions.get(index);
    }

    public void remove (int index) {
        transactions.remove(index);
    }

    public double getBalance () {
        return balance;
    }

    public Currency getCurrency () {
        return currency;
    }

    private boolean isDuplicate(int foundIndex, Transaction newTx) {
        Transaction existing = transactions.get(foundIndex);
        if (existing.equals(newTx) && existing.getAmount() == newTx.getAmount()) return true;
        for (int i = foundIndex - 1; i >= 0 && transactions.get(i).getDate().equals(newTx.getDate()); i--) {
            if (transactions.get(i).equals(newTx)) return true;
        }
        for (int i = foundIndex + 1; i < transactions.size() && transactions.get(i).getDate().equals(newTx.getDate()); i++) {
            if (transactions.get(i).equals(newTx)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return transactions.toString();
    }
}

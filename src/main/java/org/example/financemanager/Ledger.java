package org.example.financemanager;

import javafx.scene.chart.PieChart;
import javafx.scene.shape.CubicCurveTo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Ledger implements Serializable {
    private ArrayList<Transaction> transactions;
    private double balance;
    private Currency currency;
    private TreeMap<LocalDate, Double> dayBalance;

    private final static long serialVersionUID = 1L;

    public Ledger (Currency currency) {
        this.transactions = new ArrayList<>();
        this.balance = 0;
        this.currency = currency;
        this.dayBalance = new TreeMap<>();
    }

    public boolean addWithDuplicityCheck (Transaction transaction) {
        try {
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
        } catch (Exception e) {
            return false;
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
            } else {
                transactions.add(-index - 1, transaction);
            }
            balance += transaction.getAmount();

            LocalDate localDate = transaction.getDate().toLocalDate();
            updateDayBalance(transaction.getAmount(), localDate);
            return true;
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

    public int getSize () {
        return transactions.size();
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

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public double getFloorBalance (LocalDate date) {
        if (dayBalance.containsKey(date)) {
            return dayBalance.get(date);
        } else {
            try {
                Double balance = dayBalance.floorEntry(date).getValue();
                if (balance != null) {
                    return balance;
                }
            } catch (Exception e) {}
            return 0;
        }
    }
    public double getCeilingBalance (LocalDate date) {
        if (dayBalance.containsKey(date)) {
            return dayBalance.get(date);
        } else {
            Double balance = dayBalance.ceilingEntry(date).getValue();
            if (balance != null) {
                return balance;
            }
            return getFloorBalance(date);
        }
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

    private void updateDayBalance (double balance, LocalDate date) {
        if (dayBalance.containsKey(date)) {
            dayBalance.put(date, balance);
        } else {
            dayBalance.put(date, getFloorBalance(date) + balance);
        }
        Map<LocalDate, Double> future = dayBalance.tailMap(date, false);
        for (Map.Entry<LocalDate, Double> entry : future.entrySet()) {
            entry.setValue(entry.getValue() + balance);
        }
    }

    @Override
    public String toString() {
        return transactions.toString();
    }
}

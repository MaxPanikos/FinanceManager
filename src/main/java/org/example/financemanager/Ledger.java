package org.example.financemanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Ledger implements Serializable {
    private ArrayList<Transaction> transactions;
    private double balance;

    private final static long serialVersionUID = 1L;

    public Ledger () {
        this.transactions = new ArrayList<>();
        this.balance = 0;
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

    public Transaction get (int index) {
        return transactions.get(index);
    }

    public void remove (int index) {
        transactions.remove(index);
    }

    public double getBalance () {
        return balance;
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

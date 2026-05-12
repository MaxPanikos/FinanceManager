package org.example.financemanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Ledger implements Serializable {
    private ArrayList<Transaction> transactions;

    private final static long serialVersionUID = 1L;

    public Ledger () {
        this.transactions = new ArrayList<>();
    }

    public boolean add (Transaction transaction) {
        int index = Collections.binarySearch(transactions, transaction);
        if (index >= 0) {
            if (isDuplicate(index, transaction)) {
                return false;
            }
            transactions.add(index, transaction);
            return true;
        } else {
            transactions.add(-index - 1, transaction);
            return true;
        }
    }

    public Transaction get (int index) {
        return transactions.get(index);
    }

    public void remove (int index) {
        transactions.remove(index);;
    }

    private boolean isDuplicate(int foundIndex, Transaction newTx) {
        if (transactions.get(foundIndex).getHash() == newTx.getHash()) return true;
        for (int i = foundIndex - 1; i >= 0 && transactions.get(i).getDate().equals(newTx.getDate()); i--) {
            if (transactions.get(i).getHash() == newTx.getHash()) return true;
        }
        for (int i = foundIndex + 1; i < transactions.size() && transactions.get(i).getDate().equals(newTx.getDate()); i++) {
            if (transactions.get(i).getHash() == newTx.getHash()) return true;
        }
        return false;
    }
}

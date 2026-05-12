package org.example.financemanager;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction implements Serializable, Comparable<Transaction> {
    private int hash;
    private double amount;
    private TransactionTypes type;
    private LocalDateTime date;
    private int order;

    private final static long serialVersionUID = 1L;

    public Transaction(double amount, TransactionTypes type, LocalDateTime date) {
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.order = 0;
        this.hash = hashCode();
    }

    public Transaction(double amount, TransactionTypes type, LocalDateTime date, int order) {
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.order = order;
        this.hash = hashCode();
    }

    public double getAmount() {
        return amount;
    }

    public TransactionTypes getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getHash () {
        return hash;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, type, date, order);
    }

    @Override
    public int compareTo(Transaction o) {
        return this.date.compareTo(o.getDate());
    }

    @Override
    public String toString() {
        return type + " - " + amount + " - " + date;
    }
}

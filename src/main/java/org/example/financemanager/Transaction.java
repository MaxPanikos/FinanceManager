package org.example.financemanager;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction implements Serializable, Comparable<Transaction> {
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
    }

    public Transaction(double amount, TransactionTypes type, LocalDateTime date, int order) {
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.order = order;
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

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public int compareTo(Transaction o) {
        return this.date.compareTo(o.getDate());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Transaction that = (Transaction) object;
        return Double.compare(amount, that.amount) == 0 && order == that.order && type == that.type && Objects.equals(date, that.date);
    }

    @Override
    public String toString() {
        return type + " - " + amount + " - " + date;
    }
}

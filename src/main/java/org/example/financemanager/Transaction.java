package org.example.financemanager;

import java.time.LocalDateTime;

public class Transaction {
    private long amount;
    private TransactionTypes type;
    private LocalDateTime date;

    public Transaction(long amount, TransactionTypes type, LocalDateTime date) {
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public long getAmount() {
        return amount;
    }

    public TransactionTypes getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }
}

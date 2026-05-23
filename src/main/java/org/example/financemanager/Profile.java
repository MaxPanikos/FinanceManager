package org.example.financemanager;

import java.io.Serializable;
import java.util.Currency;

public class Profile implements Serializable {
    private String username;
    private String imagePath;
    private Ledger ledger;

    private static final long serialVersionUID = 1L;

    public Profile(String username, String currencyCode) {
        this.username = username;
        this.imagePath = null;
        this.ledger = new Ledger(Currency.getInstance(currencyCode));
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUsername() {
        return username;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Ledger getLedger() {
        return ledger;
    }

    @Override
    public String toString() {
        return username;
    }
}

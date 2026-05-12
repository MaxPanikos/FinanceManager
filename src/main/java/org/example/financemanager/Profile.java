package org.example.financemanager;

import javafx.scene.control.ProgressIndicator;

import java.io.Serializable;

public class Profile implements Serializable {
    private String username;
    private String imagePath;
    private Ledger ledger;

    private static final long serialVersionUID = 1L;

    public Profile(String username) {
        this.username = username;
        this.imagePath = null;
        this.ledger = new Ledger();
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

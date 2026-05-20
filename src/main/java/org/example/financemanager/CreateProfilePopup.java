package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

import java.io.File;
import java.io.IOException;
import java.util.Currency;

public class CreateProfilePopup extends VBox {
    private ProfileChooserView page;
    @FXML
    protected TextField usernameField;
    @FXML
    protected Label responseLabel;
    @FXML
    protected SearchableComboBox<String> currencyBox;

    @FXML
    public void initialize() {
        String[] currencies = new String[Currency.getAvailableCurrencies().size()];
        int i = 0;
        for (Currency currency : Currency.getAvailableCurrencies()) {
            currencies[i] = currency.getCurrencyCode();
            i++;
        }
        currencyBox.getItems().addAll(currencies);
        currencyBox.setValue("CZK");
    }

    public CreateProfilePopup(ProfileChooserView page) {
        this.page = page;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("create-profile-view.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void createProfile () {
        String username = usernameField.getText();
        String currency = currencyBox.getValue();
        if (username.isBlank()) {
            responseLabel.setText("Zadejte prosím uživatelské jméno");
            return;
        } else if (FileManager.exists(username, FileManager.profilesPath)) {
            responseLabel.setText("Uživatel s tímto jménem již existuje");
            return;
        } else if (currency == null || currency.isBlank()) {
            responseLabel.setText("Nastavte prosím měnu");
            return;
        }
        Profile p = new Profile(username, currency);
        try {
            FileManager.save(p, FileManager.profilesPath);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        page.login(p);
    }
    @FXML
    protected void closePopup () {
        page.closePopup();
    }
}

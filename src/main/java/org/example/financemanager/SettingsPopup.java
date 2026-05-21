package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SettingsPopup extends VBox {
    private AppView appView;

    public SettingsPopup(AppView appView) {
        super();
        this.appView = appView;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settings-view.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void closePopup() {
        appView.hidePopup();
    }

    @FXML
    private void saveSettings() {
    }
}

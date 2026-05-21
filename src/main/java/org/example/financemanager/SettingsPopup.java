package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class SettingsPopup extends VBox {
    private AppView appView;
    private Main main;

    public SettingsPopup(AppView appView, Main main) {
        super();
        this.appView = appView;
        this.main = main;
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
    private void removeAccount() {
        FileManager.removeProfile(appView.getProfile().getUsername(), FileManager.profilesPath);
        main.setPane(new ProfileChooserView(main));
    }
    @FXML
    private void logout () {
        main.setPane(new ProfileChooserView(main));
    }

    @FXML
    private void saveSettings() {
    }
}

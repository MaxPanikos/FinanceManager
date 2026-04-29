package org.example.financemanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ProfileChooserView extends VBox {
    public ProfileChooserView() {
        super();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile-chooser-view.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

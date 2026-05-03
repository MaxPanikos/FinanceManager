package org.example.financemanager;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;

public class ProfileChooserView extends VBox {
    private ArrayList<Profile> profiles;

    @FXML
    private TilePane profilesPane;

    @FXML
    public void initialize() {
        try {
            profiles = FileManager.loadProfiles(FileManager.profilesPath);
            for (Profile profile : profiles) {
                profilesPane.getChildren().add(new ProfileCell(profile));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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

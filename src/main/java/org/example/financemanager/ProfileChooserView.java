package org.example.financemanager;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.skin.ProgressIndicatorSkin;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;

public class ProfileChooserView extends StackPane {
    private ArrayList<Profile> profiles;
    private Main main;

    @FXML
    private TilePane profilesPane;
    @FXML
    private StackPane overlay, popupPane;
    @FXML
    public void initialize() {
        try {
            profiles = FileManager.loadProfiles(FileManager.profilesPath);
            for (Profile profile : profiles) {
                profilesPane.getChildren().add(new ProfileCell(profile, this));
            }
            profilesPane.getChildren().add(new AddProfileCell(this));

            popupPane.maxWidthProperty().bind(this.widthProperty().divide(3));
            popupPane.maxHeightProperty().bind(this.heightProperty().divide(3));
            popupPane.prefWidthProperty().bind(this.widthProperty().divide(3));
            popupPane.prefHeightProperty().bind(this.heightProperty().divide(3));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void login (Profile p) {
        main.setPane(new AppView(p, main));
    }

    public ProfileChooserView(Main main) {
        super();
        this.main = main;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile-chooser-view.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPopupPane (Pane popup) {
        overlay.setVisible(true);
        popupPane.getChildren().clear();
        popupPane.getChildren().add(popup);
        popupPane.setVisible(true);
    }

    public void closePopup () {
        popupPane.getChildren().clear();
        overlay.setVisible(false);
        popupPane.setVisible(false);
    }
}

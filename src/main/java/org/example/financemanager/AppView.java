package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class AppView extends StackPane {
    private Profile profile;

    @FXML
    private StackPane overlayPane, popupPane, contentPane;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button addTxButton;

    @FXML
    public void initialize () {
        this.usernameLabel.setText(profile.getUsername());
        setPage(new HomepageView());
    }

    public AppView(Profile profile) {
        this.profile = profile;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showPopup (Pane pane) {
        this.popupPane.getChildren().clear();
        this.popupPane.getChildren().add(pane);
        this.overlayPane.setVisible(true);
        this.popupPane.setVisible(true);
    }
    @FXML
    public void hidePopup () {
        this.popupPane.getChildren().clear();
        this.overlayPane.setVisible(false);
        this.popupPane.setVisible(false);
    }

    public void setPage (Pane pane) {
        this.contentPane.getChildren().clear();
        this.contentPane.getChildren().add(pane);
    }
}

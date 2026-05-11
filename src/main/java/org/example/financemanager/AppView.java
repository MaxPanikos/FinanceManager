package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AppView extends StackPane {
    private Profile profile;

    @FXML
    private StackPane overlayPane, popupPane, contentPane;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button settingsButton, addTxButton;
    @FXML
    private VBox pagesBox, sidebar;
    @FXML
    private ImageView userImage;

    @FXML
    public void initialize () {
        usernameLabel.setText(profile.getUsername());
        sidebar.prefWidthProperty().bind(this.widthProperty().multiply(0.20));
        userImage.setImage(new Image(getClass().getResourceAsStream("defaults/DefaultProfilePicture.png")));



        Button homepageButton = new Button("Domů");
        homepageButton.getStyleClass().add("menu-button");
        pagesBox.getChildren().add(homepageButton);

        Button graphsButton = new Button("Grafy");
        graphsButton.getStyleClass().add("menu-button");
        pagesBox.getChildren().add(graphsButton);

        Button investButton = new Button("Investice");
        investButton.getStyleClass().add("menu-button");
        pagesBox.getChildren().add(investButton);
    }

    public AppView(Profile profile, Main main) {
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

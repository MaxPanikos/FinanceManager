package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ProfileCell extends VBox {
    private Profile profile;
    private ProfileChooserView page;

    @FXML
    private ImageView profileImage;
    @FXML
    private Label usernameLabel;

    @FXML
    public void initialize() {
        try {
            if (profile.getImagePath() != null) {
                File profilePicture = new File(FileManager.profilePicturesPath + profile.getImagePath());
                if (profilePicture.exists()) {
                    profileImage.setImage(new Image(profilePicture.toURI().toString()));
                } else {
                    profileImage.setImage(new Image(getClass().getResourceAsStream("defaults/DefaultProfilePicture.png")));
                }
            } else {
                profileImage.setImage(new Image(getClass().getResourceAsStream("defaults/DefaultProfilePicture.png")));
            }
        } catch (Exception e) {
            profileImage.setImage(new Image(getClass().getResourceAsStream("defaults/DefaultProfilePicture.png")));
        }

        Rectangle clip = new Rectangle(profileImage.getFitWidth(), profileImage.getFitHeight());
        clip.setArcHeight(10);
        clip.setArcWidth(10);
        profileImage.setClip(clip);
        usernameLabel.setText(profile.getUsername());
        
    }

    public ProfileCell(Profile profile, ProfileChooserView page) {
        try {
            this.profile = profile;
            this.page = page;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile-cell.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void clickedOnProfile () {
        page.login(profile);
    }
}

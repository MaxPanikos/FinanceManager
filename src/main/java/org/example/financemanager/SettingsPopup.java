package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SettingsPopup extends DefaultPopup {
    private Main main;
    private Profile profile;

    public SettingsPopup(AppView appView, Main main) {
        super(appView);
        this.main = main;
        this.profile = appView.getProfile();
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
    private void removeAccount() {
        FileManager.removeProfile(appView.getProfile().getUsername(), FileManager.profilesPath);
        main.setPane(new ProfileChooserView(main));
    }
    @FXML
    private void logout () {
        main.setPane(new ProfileChooserView(main));
    }

    @FXML
    private void saveProfile () {
        try {
            FileManager.save(profile, FileManager.profilesPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void chooseProfilePicture () {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pictures", "*.png", "*.jpg", "*.jpeg"));
        fileChooser.setTitle("Choose profile picture");
        File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());
        if (selectedFile != null) {
            try {
                String lastImagePath = profile.getImagePath();
                profile.setImagePath(FileManager.saveProfilePicture(selectedFile, FileManager.profilePicturesPath));
                FileManager.save(profile, FileManager.profilesPath);
                appView.updateProfilePicture();
                if (lastImagePath != null && !lastImagePath.isBlank()) {
                    Path lastPath = Path.of(FileManager.profilePicturesPath, lastImagePath);
                    if (Files.exists(lastPath)) {
                        Files.delete(lastPath);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void saveSettings() {
    }
}

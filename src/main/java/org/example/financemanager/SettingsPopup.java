package org.example.financemanager;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.controlsfx.control.SearchableComboBox;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Currency;

public class SettingsPopup extends DefaultPopup {
    private Main main;
    private Profile profile;

    private PauseTransition deleteUserTimer, changeNameTimer, currencyTimer, saveTimer;

    @FXML
    private Button removeProfileButton, areYouSureButton, changeUsernameButton, changeCurrencyButton, saveButton;
    @FXML
    private HBox usernameBox, currencyBox;
    @FXML
    private Label usernameResponseLabel, saveLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private SearchableComboBox<String> currencySearchableBox;

    @FXML
    private void initialize() {
        usernameBox.setOnMouseExited(event -> {
            if (changeNameTimer == null) {
                changeNameTimer = new PauseTransition(Duration.seconds(5));
                changeNameTimer.setOnFinished(e -> {
                    usernameBox.setVisible(false);
                    changeUsernameButton.setVisible(true);
                });
            }
            changeNameTimer.playFromStart();
        });
        currencyBox.setOnMouseExited(event -> {
            if (currencyTimer == null) {
                currencyTimer = new PauseTransition(Duration.seconds(5));
                currencyTimer.setOnFinished(e -> {
                    currencyBox.setVisible(false);
                    currencyBox.setManaged(false);
                    changeCurrencyButton.setVisible(true);
                });
            }
            currencyTimer.playFromStart();
        });

        String[] currencies = new String[Currency.getAvailableCurrencies().size()];
        int i = 0;
        for (Currency currency : Currency.getAvailableCurrencies()) {
            currencies[i] = currency.getCurrencyCode();
            i++;
        }
        currencySearchableBox.getItems().addAll(currencies);
        currencySearchableBox.setValue("CZK");
    }

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

    /**
     * makes sure that user want to remove profile
     */
    @FXML
    private void areYouSure () {
        removeProfileButton.setVisible(false);
        areYouSureButton.setVisible(true);

        if (deleteUserTimer == null) {
            deleteUserTimer = new PauseTransition(Duration.seconds(5));
            deleteUserTimer.setOnFinished(event -> {
                removeProfileButton.setVisible(true);
                areYouSureButton.setVisible(false);
            });
        }
        deleteUserTimer.playFromStart();
    }

    /**
     * removes profile
     */
    @FXML
    private void removeAccount() {
        FileManager.removeProfile(appView.getProfile(), FileManager.profilesPath, FileManager.profilePicturesPath);
        main.setPane(new ProfileChooserView(main));
    }

    /**
     * log out user
     */
    @FXML
    private void logout () {
        main.setPane(new ProfileChooserView(main));
    }

    /**
     * saves profile
     */
    @FXML
    private void saveProfile () {
        try {
            FileManager.save(profile, FileManager.profilesPath);
            saveButton.setVisible(false);
            saveLabel.setVisible(true);
            if (saveTimer == null) {
                saveTimer = new PauseTransition(Duration.seconds(3));
                saveTimer.setOnFinished(e -> {
                    saveButton.setVisible(true);
                    saveLabel.setVisible(false);
                });
            }
            saveTimer.playFromStart();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * open file chooser and saves picture
     */
    @FXML
    private void chooseProfilePicture () {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".png .jpg .jpeg", "*.png", "*.jpg", "*.jpeg"));
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
    private void changeButtonToTextField () {
        changeUsernameButton.setVisible(false);
        usernameBox.setVisible(true);
    }

    @FXML
    private void changeUsername () {
        String oldUsername = profile.getUsername();
        String newUsername = usernameTextField.getText();
        if (newUsername == null || newUsername.isBlank()) {
            usernameResponseLabel.setText("Vyplňte textové pole");
            return;
        }
        if (newUsername.length() > 16) {
            usernameResponseLabel.setText("Uživatelské jméno je moc dlouhé");
            return;
        }
        boolean status = FileManager.changeUsername(oldUsername, newUsername, FileManager.profilesPath);
        if (status) {
            profile.setUsername(newUsername);
            FileManager.save(profile, FileManager.profilesPath);
            appView.update();
            appView.hidePopup();
        } else {
            usernameResponseLabel.setText("Nastala neočekávaná chyba");
        }
    }

    @FXML
    private void changeCurrencyButton () {
        changeCurrencyButton.setVisible(false);
        currencyBox.setManaged(true);
        currencyBox.setVisible(true);
    }
    @FXML
    private void changeCurrency () {
        String currencyCode = currencySearchableBox.getValue();
        if (currencyCode == null || currencyCode.isBlank()) {
            return;
        }
        profile.getLedger().setCurrency(Currency.getInstance(currencyCode));
        FileManager.save(profile, FileManager.profilesPath);
        appView.update();
    }
}

package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.PipedOutputStream;

public class AddProfileCell extends VBox {
    private ProfileChooserView page;
    public AddProfileCell(ProfileChooserView page) {
        this.page = page;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-cell.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void createProfile () {
        page.setPopupPane(new CreateProfilePopup(page));
    }
}

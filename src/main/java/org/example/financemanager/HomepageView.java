package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class HomepageView extends VBox {
    @FXML
    private TilePane tilePane;

    @FXML
    public void initialize () {

    }

    public HomepageView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

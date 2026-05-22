package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public abstract class DefaultPopup extends VBox {
    protected AppView appView;
    public DefaultPopup(AppView appView) {
        this.appView = appView;
    }

    @FXML
    public void closePopup() {
        appView.hidePopup();
    }
}

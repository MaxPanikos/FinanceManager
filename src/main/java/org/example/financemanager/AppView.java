package org.example.financemanager;

import javafx.scene.layout.StackPane;

public class AppView extends StackPane {
    private Profile profile;
    public AppView(Profile profile) {
        this.profile = profile;
    }
}

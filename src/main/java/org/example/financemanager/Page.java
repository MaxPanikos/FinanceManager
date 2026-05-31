package org.example.financemanager;

import javafx.scene.layout.StackPane;

public abstract class Page extends StackPane {
    protected AppView appView;

    public Page(AppView appView) {
        this.appView = appView;
    }

    public AppView getAppView() {
        return appView;
    }

    /**
     * call this method to update page content
     */
    public abstract void update();
}

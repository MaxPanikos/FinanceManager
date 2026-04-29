package org.example.financemanager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private StackPane root;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Finance Manager");

        this.root = new StackPane();
        Scene scene = new Scene(root, 900, 600);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });
        primaryStage.setScene(scene);

        primaryStage.setFullScreenExitHint("");

        setPane(new ProfileChooserView());

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setPane(Pane pane) {
        this.root.getChildren().clear();
        this.root.getChildren().add(pane);
    }
}

package org.example.financemanager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

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

        primaryStage.getIcons().add(new Image("file:src/main/resources/org/example/financemanager/defaults/appicon.png"));

//        FileManager.save(new Profile("susik"), FileManager.profilesPath);
//        FileManager.save(new Profile("amognus"), FileManager.profilesPath);
//        FileManager.save(new Profile("doktorsusik"), FileManager.profilesPath);

        setPane(new ProfileChooserView(this));

        primaryStage.setMinWidth(300);

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

package org.example.financemanager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.Currency;

public class Main extends Application {
    private StackPane root;

    /**
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.root = new StackPane();
        primaryStage.setTitle("Finance Manager");
        Scene scene = new Scene(root, 900, 600);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setFullScreenExitHint("");
        primaryStage.getIcons().add(new Image("file:src/main/resources/org/example/financemanager/defaults/appicon2.png"));

//        FileManager.save(new Profile("susik", "CZK"), FileManager.profilesPath);
//        FileManager.save(new Profile("amongus", "CZK"), FileManager.profilesPath);
//        FileManager.save(new Profile("doktorsusik", "CZK"), FileManager.profilesPath);


        setPane(new ProfileChooserView(this));

        primaryStage.setMinWidth(300);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * displays page
     * @param pane page you want to display
     */
    public void setPane(Pane pane) {
        this.root.getChildren().clear();
        this.root.getChildren().add(pane);
    }
}

package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;

public class AppView extends StackPane {
    private Profile profile;
    private Main main;
    private DecimalFormat df;

    @FXML
    private StackPane overlayPane, popupPane, contentPane;
    @FXML
    private Label usernameLabel, balanceLabel, todayBalanceLabel;
    @FXML
    private Button settingsButton, addTxButton;
    @FXML
    private VBox pagesBox, sidebar;
    @FXML
    private ImageView userImage;

    @FXML
    public void initialize () {
        usernameLabel.setText(profile.getUsername());
        balanceLabel.setText("Zůstatek: " + df.format(profile.getLedger().getBalance()) + " " + profile.getLedger().getCurrency().getSymbol());
        todayBalanceLabel.setText("Zůstatek (dnes): " + df.format(profile.getLedger().getFloorBalance(LocalDate.now())) + " " + profile.getLedger().getCurrency().getSymbol());
        sidebar.prefWidthProperty().bind(this.widthProperty().multiply(0.20));
        updateProfilePicture();
        userImage.setEffect(new DropShadow(10, Color.BLACK));

        popupPane.maxWidthProperty().bind(this.widthProperty().divide(3));
        popupPane.maxHeightProperty().bind(this.heightProperty().divide(3));
        popupPane.prefWidthProperty().bind(this.widthProperty().divide(3));
        popupPane.prefHeightProperty().bind(this.heightProperty().divide(3));

        Button homepageButton = new Button("Domů");
        homepageButton.getStyleClass().add("menu-button");
        homepageButton.setOnAction(event -> {
            setPage(new HomepageView(this));
        });
        pagesBox.getChildren().add(homepageButton);

        Button graphsButton = new Button("Grafy");
        graphsButton.getStyleClass().add("menu-button");
        graphsButton.setOnAction(event -> {
            setPage(new GraphsView(this));
        });
        pagesBox.getChildren().add(graphsButton);

        Button calcButton = new Button("Kalkulačka");
        calcButton.getStyleClass().add("menu-button");
        calcButton.setOnAction(event -> {
            setPage(new CalculatorView(this));
        });
        pagesBox.getChildren().add(calcButton);

        Button ledgerButton = new Button("Transakce");
        ledgerButton.getStyleClass().add("menu-button");
        ledgerButton.setOnAction(event -> {
           setPage(new TransactionView(this));
        });
        pagesBox.getChildren().add(ledgerButton);


        Glow glow = new Glow(5);
        InnerShadow innerShadow = new InnerShadow(3, Color.GREENYELLOW);
        Blend blend = new Blend(BlendMode.ADD, innerShadow, glow);
        usernameLabel.setEffect(blend);

        setPage(new HomepageView(this));
    }

    public AppView(Profile profile, Main main) {
        this.profile = profile;
        this.main = main;
        //AI
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        symbols.setDecimalSeparator(',');
        this.df = new DecimalFormat("#,##0.00", symbols);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * shows any popup extending from DefaultPopup
     * @param popup - popup you want to display
     */
    public void showPopup (DefaultPopup popup) {
        this.popupPane.getChildren().clear();
        this.popupPane.getChildren().add(popup);
        this.overlayPane.setVisible(true);
        this.popupPane.setVisible(true);
    }

    /**
     * hides visible popup
     */
    @FXML
    public void hidePopup () {
        this.popupPane.getChildren().clear();
        this.overlayPane.setVisible(false);
        this.popupPane.setVisible(false);
    }

    /**
     * shows any page extended from Page
     * @param page - page you want to show
     */
    public void setPage (Page page) {
        this.contentPane.getChildren().clear();
        this.contentPane.getChildren().add(page);
    }

    /**
     * shows add transaction popup
     */
    @FXML
    public void addTransaction () {
        showPopup(new AddTransactionView(this));
    }

    /**
     * shows settings popup
     */
    @FXML
    private void openSettings () {
        showPopup(new SettingsPopup(this, main));
    }

    public Profile getProfile () {
        return profile;
    }

    /**
     * updates profile picture on main view
     */
    public void updateProfilePicture () {
        if (profile.getImagePath() != null && Files.exists(Path.of(FileManager.profilePicturesPath + profile.getImagePath()))) {
            File file = new File(FileManager.profilePicturesPath + profile.getImagePath());
            userImage.setImage(new Image(file.toURI().toString()));
        } else {
            userImage.setImage(new Image(getClass().getResourceAsStream("defaults/DefaultProfilePicture.png")));
        }
    }

    /**
     * updates main view and any open page
     */
    public void update() {
        balanceLabel.setText("Zůstatek: " + df.format(profile.getLedger().getBalance()) + " " + profile.getLedger().getCurrency().getSymbol());
        todayBalanceLabel.setText("Zůstatek (dnes): " + df.format(profile.getLedger().getFloorBalance(LocalDate.now())) + " " + profile.getLedger().getCurrency().getSymbol());
        Page page = (Page) contentPane.getChildren().getLast();
        page.update();
    }

    public DecimalFormat getFormat() {
        return df;
    }
}

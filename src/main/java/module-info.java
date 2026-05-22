module org.example.financemanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires eu.hansolo.tilesfx;
    requires java.desktop;

    opens org.example.financemanager to javafx.fxml;
    exports org.example.financemanager;
}
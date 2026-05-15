package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class HomepageView extends VBox {
    @FXML
    private TilePane tilePane;

    @FXML
    public void initialize () {
        tilePane.getChildren().addAll(createPieChartPanel("Výdaje podle kategorií"), createBarChartPanel("Měsíční přehled"));
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

    private VBox createPieChartPanel(String title) {
        PieChart pieChart = new PieChart();
        pieChart.getData().add(new PieChart.Data("Jídlo", 3000));
        pieChart.getData().add(new PieChart.Data("Bydlení", 12000));
        pieChart.getData().add(new PieChart.Data("Zábava", 1500));

        pieChart.setTitle(title);
        pieChart.setPrefSize(300, 300);

        VBox card = new VBox(pieChart);
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-padding: 10;");
        return card;
    }
    private VBox createBarChartPanel(String title) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("2024");
        series.getData().add(new XYChart.Data<>("Leden", 20000));
        series.getData().add(new XYChart.Data<>("Únor", 18000));

        barChart.getData().add(series);
        barChart.setTitle(title);
        barChart.setPrefSize(300, 300);

        return new VBox(barChart);
    }
}

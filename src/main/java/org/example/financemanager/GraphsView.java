package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class GraphsView extends Page{
    private Ledger ledger;

    private CustomPieChart pieChart;
    private CustomBarChart barChart;
    private CustomLineChart lineChart;

    @FXML
    private VBox pieChartVBox, barChartVBox, lineChartVBox;
    @FXML
    private HBox pieChartHBox, barChartHBox, lineChartHBox;

    @FXML
    private Label rangeLabel;

    public GraphsView(AppView appView) {
        super(appView);
        this.ledger = appView.getProfile().getLedger();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("graphs-view.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        pieChart.update();
        barChart.update();
        lineChart.update();
    }

    @FXML
    private void initialize() {
        loadPieChart();
        loadBarChart();
        loadLineChart();
    }


    private void loadPieChart () {
        this.pieChart = new CustomPieChart(ledger, "Příjem");
        pieChartVBox.getChildren().add(pieChart);
        pieChartHBox.getChildren().add(new TimeSelectorView(this, pieChart.getFromDate(), pieChart.getToDate()));
    }

    private void loadBarChart () {
        this.barChart = new CustomBarChart(ledger);
        barChartVBox.getChildren().add(barChart);
        barChartHBox.getChildren().add(new TimeSelectorView(this, barChart.getFromDate(), barChart.getToDate()));
    }

    private void loadLineChart () {
        this.lineChart = new CustomLineChart(ledger);
        lineChartVBox.getChildren().add(lineChart);
        lineChartHBox.getChildren().add(new TimeSelectorView(this, lineChart.getFromDate(), lineChart.getToDate()));
    }
}

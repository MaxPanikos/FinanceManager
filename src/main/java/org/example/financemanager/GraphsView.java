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

    private TimeSelectorView pieSelector;
    private TimeSelectorView barSelector;
    private TimeSelectorView lineSelector;

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
        if (pieSelector != null) {
            pieChart.setFromDate(pieSelector.getFromDate());
            pieChart.setToDate(pieSelector.getToDate());
        }
        if (barSelector != null) {
            barChart.setFromDate(barSelector.getFromDate());
            barChart.setToDate(barSelector.getToDate());
        }
        if (lineSelector != null) {
            lineChart.setFromDate(lineSelector.getFromDate());
            lineChart.setToDate(lineSelector.getToDate());
        }
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
        this.pieSelector = new TimeSelectorView(this, pieChart.getFromDate(), pieChart.getToDate());
        pieChartHBox.getChildren().add(pieSelector);
    }

    private void loadBarChart () {
        this.barChart = new CustomBarChart(ledger);
        barChartVBox.getChildren().add(barChart);
        this.barSelector = new TimeSelectorView(this, barChart.getFromDate(), barChart.getToDate());
        barSelector.setWholeButtonVisible(false);
        barSelector.setCustomRangeButtonVisible(false);
        barChartHBox.getChildren().add(barSelector);
    }

    private void loadLineChart () {
        this.lineChart = new CustomLineChart(ledger);
        lineChartVBox.getChildren().add(lineChart);
        this.lineSelector = new TimeSelectorView(this, lineChart.getFromDate(), lineChart.getToDate());
        lineChartHBox.getChildren().add(lineSelector);
    }
}

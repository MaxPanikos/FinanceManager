package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class GraphsView extends Page{
    private Ledger ledger;
    private LocalDate fromDate, toDate;

    private CustomPieChart categoryPieChart;
    private CustomBarChart incomeBarChart;
    private CustomLineChart balanceLineChart;

    @FXML
    private VBox pieChartVBox, barChartVBox, lineChartVBox;

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
        categoryPieChart.update();
        incomeBarChart.update();
        balanceLineChart.update();
    }

    @FXML
    private void initialize() {
        loadPieChart();
        loadBarChart();
        loadLineChart();
    }


    private void loadPieChart () {
        this.categoryPieChart = new CustomPieChart(fromDate, toDate, ledger, "Příjem");
        pieChartVBox.getChildren().add(categoryPieChart);
    }

    private void loadBarChart () {
        this.incomeBarChart = new CustomBarChart(fromDate, toDate, ledger);
        barChartVBox.getChildren().add(incomeBarChart);
    }

    private void loadLineChart () {
        this.balanceLineChart = new CustomLineChart(fromDate, toDate, ledger);
        lineChartVBox.getChildren().add(balanceLineChart);
    }

    @FXML
    private void changeRange (){

    }
}

package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GraphsView extends Page{
    private Ledger ledger;

    private CustomPieChart pieChart;
    private CustomPieChart pieChart2;
    private CustomBarChart barChart;
    private CustomLineChart lineChart;

    private TimeSelectorView pieSelector;
    private TimeSelectorView pieSelector2;
    private TimeSelectorView barSelector;
    private TimeSelectorView lineSelector;

    private DateTimeFormatter formatter;

    @FXML
    private VBox pieChartVBox, pieChartVBox2, barChartVBox, lineChartVBox;
    @FXML
    private HBox pieChartHBox, pieChartHBox2, barChartHBox, lineChartHBox;
    @FXML
    private Label pieChartRange, pieChartRange2, barChartRange, lineChartRange;

    @FXML
    private void initialize() {
        loadPieChart();
        loadPieChart2();
        loadBarChart();
        loadLineChart();
    }

    public GraphsView(AppView appView) {
        super(appView);
        this.ledger = appView.getProfile().getLedger();
        this.formatter = DateTimeFormatter.ofPattern("d.M. yyyy");
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
    public void update () {
        if (pieSelector != null  && !(pieChart.getFromDate().isEqual(pieSelector.getFromDate()) && pieChart.getToDate().isEqual(pieSelector.getToDate()))) {
            pieChart.setFromDate(pieSelector.getFromDate());
            pieChart.setToDate(pieSelector.getToDate());
            pieChart.update();
            pieChartRange.setText(pieChart.getFromDate().format(formatter) + " - " + pieChart.getToDate().format(formatter));
        }
        if (pieSelector2 != null  && !(pieChart2.getFromDate().isEqual(pieSelector2.getFromDate()) && pieChart2.getToDate().isEqual(pieSelector2.getToDate()))) {
            pieChart2.setFromDate(pieSelector2.getFromDate());
            pieChart2.setToDate(pieSelector2.getToDate());
            pieChart2.update();
            pieChartRange2.setText(pieChart2.getFromDate().format(formatter) + " - " + pieChart2.getToDate().format(formatter));
        }
        if (barSelector != null && !(barChart.getFromDate().isEqual(barSelector.getFromDate()) && barChart.getToDate().isEqual(barSelector.getToDate()))) {
            barChart.setFromDate(barSelector.getFromDate());
            barChart.setToDate(barSelector.getToDate());
            barChart.update();
            barChartRange.setText(barChart.getFromDate().format(formatter) + " - " + barChart.getToDate().format(formatter));
        }
        if (lineSelector != null && !(lineChart.getFromDate().isEqual(lineSelector.getFromDate()) && lineChart.getToDate().isEqual(lineSelector.getToDate()))) {
            lineChart.setFromDate(lineSelector.getFromDate());
            lineChart.setToDate(lineSelector.getToDate());
            lineChart.update();
            lineChartRange.setText(lineChart.getFromDate().format(formatter) + " - " + lineChart.getToDate().format(formatter));
        }
    }

    private void loadPieChart () {
        this.pieChart = new CustomPieChart(ledger, "Příjem");
        pieChartVBox.getChildren().add(pieChart);
        this.pieSelector = new TimeSelectorView(this, pieChart.getFromDate(), pieChart.getToDate());
        pieChartHBox.getChildren().add(pieSelector);
        pieChartRange.setText(pieChart.getFromDate().format(formatter) + " - " + pieChart.getToDate().format(formatter));
    }
    private void loadPieChart2 () {
        this.pieChart2 = new CustomPieChart(ledger, "Výdaj");
        pieChartVBox2.getChildren().add(pieChart2);
        this.pieSelector2 = new TimeSelectorView(this, pieChart2.getFromDate(), pieChart2.getToDate());
        pieChartHBox2.getChildren().add(pieSelector2);
        pieChartRange2.setText(pieChart2.getFromDate().format(formatter) + " - " + pieChart2.getToDate().format(formatter));
    }

    private void loadBarChart () {
        this.barChart = new CustomBarChart(ledger);
        barChartVBox.getChildren().add(barChart);
        this.barSelector = new TimeSelectorView(this, barChart.getFromDate(), barChart.getToDate());
        barSelector.setWholeButtonVisible(false);
        barSelector.setCustomRangeButtonVisible(false);
        barChartHBox.getChildren().add(barSelector);
        barChartRange.setText(barChart.getFromDate().format(formatter) + " - " + barChart.getToDate().format(formatter));
    }

    private void loadLineChart () {
        this.lineChart = new CustomLineChart(ledger);
        lineChartVBox.getChildren().add(lineChart);
        this.lineSelector = new TimeSelectorView(this, lineChart.getFromDate(), lineChart.getToDate());
        lineChartHBox.getChildren().add(lineSelector);
        lineChartRange.setText(lineChart.getFromDate().format(formatter) + " - " + lineChart.getToDate().format(formatter));
    }
}

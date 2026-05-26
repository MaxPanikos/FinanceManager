package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class GraphsView extends Page{
    private Ledger ledger;
    private LocalDate fromDate, toDate;

    @FXML
    private PieChart categoryPieChart;
    @FXML
    private BarChart<String, Number> incomeBarChart;
    @FXML
    private LineChart<String, Number> balanceLineChart;

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
        loadGraphsData();
    }

    @FXML
    private void initialize() {
        loadGraphsData();
    }

    private void loadGraphsData() {
        loadPieChart();
        loadBarChart();
        loadLineChart();
    }

    private void loadPieChart () {
        ArrayList<Transaction> transactions;
        if (fromDate == null || toDate == null) {
            transactions = ledger.getTransactions();
        } else {
            transactions = ledger.getTransactionsInRange(fromDate.atStartOfDay(), toDate.atStartOfDay());
        }

        HashMap<TransactionTypes, PieChart.Data> dataMap = new HashMap<>();

        for (TransactionTypes type : TransactionTypes.values()) {
            if (type.getType().equals("Příjem")) {
                dataMap.put(type, new PieChart.Data(type.getLabel(), 0.0));
            }
        }

        for (Transaction transaction : transactions) {
            TransactionTypes type = transaction.getType();
            if (dataMap.containsKey(type)) {
                double currentAmount = dataMap.get(type).getPieValue();
                dataMap.get(type).setPieValue(currentAmount + transaction.getAmount());
            }
        }
        categoryPieChart.getData().clear();
        for (PieChart.Data data : dataMap.values()) {
            if (data.getPieValue() > 0.0) {
                categoryPieChart.getData().add(data);
            }
        }
    }

    private void loadBarChart () {
        XYChart.Series<String, Number> prijmy = new XYChart.Series<>();
        prijmy.setName("Příjmy");
        prijmy.getData().add(new XYChart.Data<>("Leden", 35000));
        prijmy.getData().add(new XYChart.Data<>("Únor", 36500));
        prijmy.getData().add(new XYChart.Data<>("Březen", 35000));

        XYChart.Series<String, Number> vydaje = new XYChart.Series<>();
        vydaje.setName("Výdaje");
        vydaje.getData().add(new XYChart.Data<>("Leden", 22000));
        vydaje.getData().add(new XYChart.Data<>("Únor", 28000));
        vydaje.getData().add(new XYChart.Data<>("Březen", 20800));

        incomeBarChart.getData().addAll(prijmy, vydaje);
    }

    private void loadLineChart () {
        XYChart.Series<String, Number> zustatek = new XYChart.Series<>();
        zustatek.setName("Stav účtu");
        zustatek.getData().add(new XYChart.Data<>("1.1.", 50000));
        zustatek.getData().add(new XYChart.Data<>("15.1.", 62000));
        zustatek.getData().add(new XYChart.Data<>("1.2.", 58000));
        zustatek.getData().add(new XYChart.Data<>("15.2.", 71000));

        balanceLineChart.getData().add(zustatek);
    }

    @FXML
    private void changeRange (){

    }
}

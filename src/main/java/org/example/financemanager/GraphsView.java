package org.example.financemanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class GraphsView extends Page{
    @FXML
    private PieChart categoryPieChart;
    @FXML
    private BarChart<String, Number> incomeExpenseBarChart;
    @FXML
    private LineChart<String, Number> balanceLineChart;

    public GraphsView(AppView appView) {
        super(appView);
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
        categoryPieChart.getData().addAll(
                new PieChart.Data("Potraviny", 4500),
                new PieChart.Data("Bydlení", 12000),
                new PieChart.Data("Zábava", 2500),
                new PieChart.Data("Doprava", 1800)
        );

        XYChart.Series<String, Number> prijmy = new XYChart.Series<>();
        prijmy.setName("Příjmy");
        prijmy.getData().add(new XYChart.Data<>("Leden", 35000));
        prijmy.getData().add(new XYChart.Data<>("Únor", 36500));
        prijmy.getData().add(new XYChart.Data<>("Březen", 35000));

        XYChart.Series<String, Number> vydaje = new XYChart.Series<>();
        vydaje.setName("Výdaje");
        vydaje.getData().add(new XYChart.Data<>("Leden", 22000));
        vydaje.getData().add(new XYChart.Data<>("Únor", 28000)); // v únoru se utrácelo víc...
        vydaje.getData().add(new XYChart.Data<>("Březen", 20800));

        incomeExpenseBarChart.getData().addAll(prijmy, vydaje);

        XYChart.Series<String, Number> zustatek = new XYChart.Series<>();
        zustatek.setName("Stav účtu");
        zustatek.getData().add(new XYChart.Data<>("1.1.", 50000));
        zustatek.getData().add(new XYChart.Data<>("15.1.", 62000));
        zustatek.getData().add(new XYChart.Data<>("1.2.", 58000));
        zustatek.getData().add(new XYChart.Data<>("15.2.", 71000));

        balanceLineChart.getData().add(zustatek);
    }
}

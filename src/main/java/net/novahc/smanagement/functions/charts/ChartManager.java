package net.novahc.smanagement.functions.charts;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ChartManager {
    public void initBarChart(BarChart<String,Number> chart, String xName, String yName, HashMap<Integer, Integer> chartValues){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xName);
        yAxis.setLabel(yName);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Test");

        for(Integer key: chartValues.keySet()){
            series.getData().add(new XYChart.Data<>(key.toString(), chartValues.get(key)));
            System.out.println(key.toString()+ " : " + chartValues.get(key));
        }
        chart.getData().add(series);
    }
    public void updateBarChart(BarChart<String,Number> chart, HashMap<Integer, Integer> chartValues){
        ObservableList<XYChart.Series<String, Number>> oldData = chart.getData();

        XYChart.Series<String, Number> newData = new XYChart.Series<>();
        newData.setName("Test");

        for(Integer key: chartValues.keySet()){
            newData.getData().add(new XYChart.Data<>(key.toString(), chartValues.get(key)));
            System.out.println(key.toString()+ " : " + chartValues.get(key));
        }
        chart.getData().removeAll(oldData);
        chart.getData().addAll(newData);

    }
}

package net.novahc.smanagement.functions.charts;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import net.novahc.smanagement.functions.Users.Student;

import java.util.ArrayList;
import java.util.HashMap;

public class ChartManager {
    public void initBarChart(BarChart<String,Number> chart, String xName, String yName, HashMap<Integer, Integer> chartValues){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xName);
        yAxis.setLabel(yName);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("# of Students");

        for(Integer key: chartValues.keySet()){
            series.getData().add(new XYChart.Data<>(key.toString(), chartValues.get(key)));
            System.out.println(key.toString()+ " : " + chartValues.get(key)); //TODO Debugging business
        }
        System.out.println();
        chart.getData().add(series);
    }
    public void updateBarChart(BarChart<String,Number> chart, HashMap<Integer, Integer> chartValues){
        ObservableList<XYChart.Series<String, Number>> oldData = chart.getData();

        XYChart.Series<String, Number> newData = new XYChart.Series<>();
        newData.setName("# of Students");

        for(Integer key: chartValues.keySet()){
            newData.getData().add(new XYChart.Data<>(key.toString(), chartValues.get(key)));
            System.out.println(key.toString()+ " : " + chartValues.get(key));
        }
        chart.getData().removeAll(oldData);
        chart.getData().addAll(newData);

    }

    public void initPieChart(PieChart pieChart, ArrayList<Student> students) {
        pieChart.getData().addAll(getTotals(pieChart, students));
    }
    public void updatePieChart(PieChart pieChart, ArrayList<Student> students) {
        pieChart.getData().removeAll(pieChart.getData());
        pieChart.getData().addAll(getTotals(pieChart, students));
    }

    private PieChart.Data[] getTotals(PieChart pieChart, ArrayList<Student> students) {
        int[] totals = new int[2];
        for(Student student : students){
            if(student.isPresent()){
                totals[0]+=1;
            } else{
                totals[1]+=1;
            }
        }
        PieChart.Data trueData = new PieChart.Data("Present",totals[0]);
        PieChart.Data falseData = new PieChart.Data("Not Present",totals[1]);
        return new PieChart.Data[]{trueData,falseData};
    }
}

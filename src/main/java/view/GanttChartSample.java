package view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;

// TODO: use date for x-axis
public class GanttChartSample {

    private GanttChart<Number,String> _chart;

    public GanttChartSample(){

        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        String[] machines = new String[] { "Machine 1", "Machine 2", "Machine 3" };

        _chart = new GanttChart<>(xAxis, yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(machines)));

        _chart.setTitle("Machine Monitoring");
        _chart.setLegendVisible(false);
        _chart.setBlockHeight( 50);

        String machine;

        machine = machines[0];
        XYChart.Series series1 = new XYChart.Series<>();
        series1.getData().add(new XYChart.Data<>(0, machine, new GanttChart.ExtraData( 1, "status-red")));
        series1.getData().add(new XYChart.Data<>(1, machine, new GanttChart.ExtraData( 1, "status-green")));
        series1.getData().add(new XYChart.Data<>(2, machine, new GanttChart.ExtraData( 1, "status-red")));
        series1.getData().add(new XYChart.Data<>(3, machine, new GanttChart.ExtraData( 1, "status-green")));
        series1.getData().add(new XYChart.Data<>(4,machine,new GanttChart.ExtraData(1, "status-green")));


        machine = machines[1];
        XYChart.Series series2 = new XYChart.Series();
        series2.getData().add(new XYChart.Data<>(0, machine, new GanttChart.ExtraData( 1, "status-green")));
        series2.getData().add(new XYChart.Data<>(1, machine, new GanttChart.ExtraData( 1, "status-green")));
        series2.getData().add(new XYChart.Data<>(2, machine, new GanttChart.ExtraData( 2, "status-red")));

        machine = machines[2];
        XYChart.Series series3 = new XYChart.Series();
        series3.getData().add(new XYChart.Data<>(0, machine, new GanttChart.ExtraData( 1, "status-blue")));
        series3.getData().add(new XYChart.Data<>(1, machine, new GanttChart.ExtraData( 2, "status-red")));
        series3.getData().add(new XYChart.Data<>(3, machine, new GanttChart.ExtraData( 1, "status-green")));

        _chart.getData().addAll(series1, series2, series3);

        _chart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());
    }


    public GanttChart get_chart() {
        return _chart;
    }
}
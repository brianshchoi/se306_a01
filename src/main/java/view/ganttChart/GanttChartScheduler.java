package view.ganttChart;

import app.IAlgorithm;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import scheduleModel.IProcessor;
import scheduleModel.ISchedule;
import taskModel.Task;
import view.nodeTree.NodeColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GanttChartScheduler implements InvalidationListener {

    private List<IProcessor> _processors;
    private GanttChart<Number,String> _chart;

    public GanttChartScheduler(ISchedule schedule){
        _processors = schedule.getProcessors();
        createChart();
    }

    private void createChart(){
        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        // Makes List of processor names
        String[] processorNames = makeProcessorNames();

        // Chart set up
        _chart = new GanttChart<>(xAxis, yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.WHITE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.WHITE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(processorNames)));

        _chart.setTitle("Task Schedule");
        _chart.setLegendVisible(false);
        _chart.setBlockHeight( 50 - _processors.size() * 1.3);

        // Make series list then add to chart
        List<XYChart.Series> seriesList = makeSeriesList(processorNames);
        for(XYChart.Series s : seriesList){
          _chart.getData().add(s);
        }

        _chart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());
    }

    private String[] makeProcessorNames() {
        String[] processorNames = new String[_processors.size()];
        for(int i = 0; i < _processors.size(); i++){
            processorNames[i] = "processor_" + _processors.get(i).getId();
        }
        return processorNames;
    }

    private List<XYChart.Series> makeSeriesList(String[] procName){
        // for each processors
        List<XYChart.Series> seriesList = new ArrayList<>();
        List<NodeColor> colors = Arrays.asList(NodeColor.values());
        for(int i = 0; i < _processors.size(); i++){
            XYChart.Series series = new XYChart.Series<>();
            // add each task to series
            List<Task> listOfTasks = _processors.get(i).getTasks();
            for(Task t : listOfTasks) {
                int startTime = _processors.get(i).getStartTimeOf(t);
                GanttChart.ExtraData ganttChart = new GanttChart.ExtraData( t.getWeight(), colors.get(i), t);
                XYChart.Data<Number, String> taskData = new XYChart.Data<>(startTime, procName[i], ganttChart);
    //            displayLabelForData(taskData, t, ganttChart);
                series.getData().add(taskData);

            }
            seriesList.add(series);
        }
        return seriesList;
    }
    
    public GanttChart getChart() {
        return _chart;
    }

    @Override
    public void invalidated(Observable observable) {
        ISchedule schedule = ((IAlgorithm) observable).getBestSchedule();
        schedule.debug();
        System.out.println("INVALIDATION EVENT FIRED!");
    }
}

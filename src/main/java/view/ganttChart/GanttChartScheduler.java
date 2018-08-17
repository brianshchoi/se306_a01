package view.ganttChart;

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

/* This class passes in the necessary information to the GanttChart to render the chart.
* It finds the number of processors, what tasks are on which processor  and the color
* to be rendered for the processor. */
public class GanttChartScheduler {

    private List<IProcessor> processors;
    private GanttChart<Number,String> chart;

    // Assigns the schedule that is going to be charted to the constructor
    public GanttChartScheduler(ISchedule schedule){
        processors = schedule.getProcessors();
        createChart();
    }

    private void createChart(){
        // Makes List of processor names
        String[] processorNames = makeProcessorNames();

        // Chart set up
        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        chart = new GanttChart<>(xAxis, yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.WHITE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.WHITE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(processorNames)));

        chart.setTitle("Click to zoom");
        chart.setLegendVisible(false);

        // Variable block height depending on the number of processors so blocks don't overlap
        chart.setBlockHeight( 20 + (10 / processors.size()));

        // Make series list then add to chart
        List<XYChart.Series> seriesList = makeSeriesList(processorNames);
        for(XYChart.Series series : seriesList){
          chart.getData().add(series);
        }

        // Retrieves style sheet for colouring the processors
        chart.getStylesheets().add(getClass().getResource("ganttchart.css").toExternalForm());
    }

    // Creates the names of the processors to be labelled on the Y-axis
    private String[] makeProcessorNames() {
        String[] processorNames = new String[processors.size()];
        for(int i = 0; i < processors.size(); i++){
            processorNames[i] = "P" + processors.get(i).getId();
        }
        return processorNames;
    }

    private List<XYChart.Series> makeSeriesList(String[] processorNames){
        // for each processor
        List<XYChart.Series> seriesList = new ArrayList<>();
        List<NodeColor> colors = Arrays.asList(NodeColor.values());

        // Select the colour of the processor from the NodeColour Enum
        int colorIndex = 0;
        for(int i = 0; i < processors.size(); i++){
            XYChart.Series series = new XYChart.Series<>();
            // add each task to series
            List<Task> listOfTasks = processors.get(i).getTasks();
            for(Task task : listOfTasks) {

                // Pass in the data the chart is required for each task
                int startTime = processors.get(i).getStartTimeOf(task);
                GanttChart.ExtraData ganttChart = new GanttChart.ExtraData( task.getWeight(), colors.get(colorIndex), task);
                XYChart.Data<Number, String> taskData = new XYChart.Data<>(startTime, processorNames[i], ganttChart);
                series.getData().add(taskData);

            }
            seriesList.add(series);

            // Loops through the NodeColor Enum if there are more processors than colors
            colorIndex++;
            if (colorIndex == colors.size() - 1) {
                colorIndex = 0;
            }
        }
        return seriesList;
    }
    
    public GanttChart getChart() {
        return chart;
    }
}

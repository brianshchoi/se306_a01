package view.ganttChart;

import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import taskModel.Task;
import view.nodeTree.NodeColor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
// Acknowledgement:
// A lot of stuff here is inspired by Roland on Stack Overflow.
// We used his ideas and modified it.  See link below:
// https://stackoverflow.com/questions/27975898/gantt-chart-from-scratch

/* This class does all the rendering of the Gantt Chart.
* After being given the extra parameters required such as the weight of a task,
* the colour that the task/processor should be and the actual task, it renders
* the task on a particular position on the chart relating to which processor
* and the starting time of the task.*/
public class GanttChart<X,Y> extends XYChart<X,Y> {

    private Rectangle rectangle;
    private double blockHeight = 10;

    // Extra parameters for rendering a task
    public static class ExtraData {

        // Fields
        private long length;
        private String styleClass;
        private Task task;

        // Passes in the extra data each processor needs to be rendered for the chart
        public ExtraData(long lengthMs, NodeColor color, Task task) {
            super();
            switch (color) {
                case RED:
                    this.styleClass = "status-red";
                    break;
                case ORANGE:
                    this.styleClass = "status-orange";
                    break;
                case YELLOW:
                    this.styleClass = "status-yellow";
                    break;
                case GREEN:
                    this.styleClass = "status-green";
                    break;
                case BLUE:
                    this.styleClass = "status-blue";
                    break;
                case INDIGO:
                    this.styleClass = "status-indigo";
                    break;
                case VIOLET:
                    this.styleClass = "status-violet";
                    break;
                case CYAN:
                    this.styleClass = "status-cyan";
                    break;
                case MAGENTA:
                    this.styleClass = "status-magenta";
                    break;
                default:
                case WHITE:
                    this.styleClass = "status-white";
                    break;
            }
            this.length = lengthMs;
            this.task = task;
        }

        public Task getTask(){
            return task;
        }

        public long getLength() {
            return length;
        }
        public void setLength(long length) {
            this.length = length;
        }

        public String getStyleClass() {
            return styleClass;
        }
        public void setStyleClass(String styleClass) {
            this.styleClass = styleClass;
        }
    }

    public GanttChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
        super(xAxis,yAxis);
        setData(FXCollections.<Series<X,Y>>observableArrayList());
    }

    private static String getStyleClass( Object obj) {
        return ((ExtraData) obj).getStyleClass();
    }
    private static double getLength( Object obj) {
        return ((ExtraData) obj).getLength();
    }
    private static Task getTask( Object obj) {
        return ((ExtraData) obj).getTask();
    }

    public double getBlockHeight() {
        return blockHeight;
    }
    public void setBlockHeight( double blockHeight) {
        this.blockHeight = blockHeight;
    }

    // Place all the tasks on the bar for each processor
    @Override protected void layoutPlotChildren() {
        for (int seriesIndex=0; seriesIndex < getData().size(); seriesIndex++) {
            Series<X,Y> series = getData().get(seriesIndex);
            Iterator<Data<X,Y>> iter = getDisplayedDataIterator(series);
            while(iter.hasNext()) {
                Data<X,Y> item = iter.next();

                // Find the position to layout the X and Y axes
                double x = getXAxis().getDisplayPosition(item.getXValue());
                double y = getYAxis().getDisplayPosition(item.getYValue());

                // Get the shape of the rectangle to be drawn
                Node block = item.getNode();
                if (block != null) {
                    if (block instanceof StackPane) {
                        StackPane region = (StackPane)item.getNode();
                        if (region.getShape() == null) {
                            rectangle = new Rectangle( getLength( item.getExtraValue()), getBlockHeight());
                        } else if (region.getShape() instanceof Rectangle) {
                            rectangle = (Rectangle)region.getShape();
                        } else {
                            return;
                        }

                        // Set the size of the rectangle block for each task. Height is dependent on the number
                        // of processors and width is dependent on the task weight
                        rectangle.setWidth( getLength( item.getExtraValue()) * ((getXAxis() instanceof NumberAxis) ? Math.abs(((NumberAxis)getXAxis()).getScale()) : 1));
                        rectangle.setHeight(getBlockHeight() * ((getYAxis() instanceof NumberAxis) ? Math.abs(((NumberAxis)getYAxis()).getScale()) : 1));
                        y -= getBlockHeight() / 2.0;

                        // The region doesn't update itself when the shape is mutated in place, so we
                        // null out and then restore the shape in order to force invalidation.
                        region.setShape(null);
                        region.setShape(rectangle);
                        region.setScaleShape(false);
                        region.setCenterShape(false);
                        region.setCacheShape(false);

                        block.setLayoutX(x);
                        block.setLayoutY(y);

                        // Set the text for the weight of each task to the center of the rectangle
                        Text text = new Text(getTask(item.getExtraValue()).getName());
                        text.setFill(Color.WHITE);
                        Group group = new Group(text);
                        group.setTranslateY(rectangle.getHeight()/2);
                        group.setTranslateX(rectangle.getWidth()/2);
                        region.getChildren().add(group);

                    }
                }
            }
        }
    }

// Put the rectangle in a node container to be styled
    private Node createContainer(Series<X, Y> series, int seriesIndex, final Data<X,Y> item, int itemIndex) {
        Node container = item.getNode();
        if (container == null) {
            container = new StackPane();
            item.setNode(container);
        }
        container.getStyleClass().add( getStyleClass( item.getExtraValue()));

        return container;
    }

    // Change the range of the axis depending on the makespan of the schedule
    @Override protected void updateAxisRange() {
        final Axis<X> xa = getXAxis();
        final Axis<Y> ya = getYAxis();
        List<X> xData = null;
        List<Y> yData = null;
        if(xa.isAutoRanging()) xData = new ArrayList<X>();
        if(ya.isAutoRanging()) yData = new ArrayList<Y>();
        if(xData != null || yData != null) {
            for(Series<X,Y> series : getData()) {
                for(Data<X,Y> data: series.getData()) {
                    if(xData != null) {
                        xData.add(data.getXValue());
                        xData.add(xa.toRealValue(xa.toNumericValue(data.getXValue()) + getLength(data.getExtraValue())));
                    }
                    if(yData != null){
                        yData.add(data.getYValue());
                    }
                }
            }
            if(xData != null) xa.invalidateRange(xData);
            if(yData != null) ya.invalidateRange(yData);
        }
    }

    @Override protected void dataItemAdded(Series<X,Y> series, int itemIndex, Data<X,Y> item) {
        Node block = createContainer(series, getData().indexOf(series), item, itemIndex);
        getPlotChildren().add(block);
    }

    @Override protected  void dataItemRemoved(final Data<X,Y> item, final Series<X,Y> series) {
        final Node block = item.getNode();
        getPlotChildren().remove(block);
        removeDataItemFromDisplay(series, item);
    }

    @Override protected void dataItemChanged(Data<X, Y> item) {
    }

    @Override protected  void seriesAdded(Series<X,Y> series, int seriesIndex) {
        for (int j=0; j<series.getData().size(); j++) {
            Data<X,Y> item = series.getData().get(j);
            Node container = createContainer(series, seriesIndex, item, j);
            getPlotChildren().add(container);
        }
    }

    @Override protected  void seriesRemoved(final Series<X,Y> series) {
        for (Data<X,Y> d : series.getData()) {
            final Node container = d.getNode();
            getPlotChildren().remove(container);
        }
        removeSeriesFromDisplay(series);
    }

}
package app;

import scheduleModel.IProcessor;
import scheduleModel.ISchedule;
import taskModel.Task;
import taskModel.TaskModel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class DotGraph {

    private static final String OUTPUT_PREFIX = "output";
    private String _title;
    private ISchedule _schedule;
    private TaskModel _taskModel;

    public DotGraph(ISchedule schedule, TaskModel taskModel){
        this._title = OUTPUT_PREFIX;
        this._schedule = schedule;
        this._taskModel = taskModel;
    }

    DotGraph(final String title, ISchedule schedule, TaskModel taskModel){
        // Capitalise first letter of graph name and add "output" prefix
        this._title = OUTPUT_PREFIX + title.substring(0, 1).toUpperCase() + title.substring(1);
        this._schedule = schedule;
        this._taskModel = taskModel;
    }

    // This method generates the optimal schedule digraph in DOT syntax
    public void render(){
        final StringBuilder output = new StringBuilder();
        output.append(DotRenderer.openGraph(_title));

        // Iterate through all tasks
        for (Task task: _taskModel.getTasks()){
            int startTime = _schedule.getFinishTimeOf(task) - task.getWeight();
            IProcessor processor = _schedule.getProcessorOf(task);

            // Add current task
            output.append(DotRenderer.addNode(task, startTime, processor));

            // Add dependencies and their weight
            if (task.getChildren().size() > 0){
                for(Task child : task.getChildren()){
                    output.append(DotRenderer.addDependency(task, child));
                }
            }
        }

        output.append(DotRenderer.closeGraph());

        writeDotFile(output.toString());
    }

    // This method takes the graph string and outputs a dot graph with the filename
    private void writeDotFile(String graph){
        String filename = _title + ".dot";

        try (PrintStream out = new PrintStream(new FileOutputStream(filename))) {
            out.print(graph);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

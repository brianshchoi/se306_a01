package fileIO;

import scheduleModel.IProcessor;
import scheduleModel.ISchedule;
import taskModel.Task;
import taskModel.TaskModel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility object used to transform an ISchedule instance
 * into a DOT file.
 */
public class DotGraph {

    private static final String OUTPUT_PREFIX = "output";
    private String title;
    private String filename;
    private ISchedule schedule;
    private TaskModel taskModel;

    public DotGraph(String filename, String title, ISchedule schedule, TaskModel taskModel){
        // Capitalise first letter of graph name and add "output" prefix
        this.title = OUTPUT_PREFIX + title.substring(0, 1).toUpperCase() + title.substring(1);
        this.schedule = schedule;
        this.taskModel = taskModel;
        this.filename = filename;
    }

    // This method generates the optimal schedule digraph in DOT syntax
    public void render(){
        final StringBuilder output = new StringBuilder();

        // Append title of digraph and opening brace
        output.append(DotRenderer.openGraph(title));

        // Iterate through all tasks
        List<Task> tasks = taskModel.getTasks();
        DotRenderer.sortTasks(tasks);
        for (Task task: tasks){
            int startTime = schedule.getFinishTimeOf(task) - task.getWeight();
            IProcessor processor = schedule.getProcessorOf(task);

            // Add all task
            output.append(DotRenderer.addNode(task, startTime, processor));
        }

        for (Task task: tasks){
            // Add dependencies and their weight
            if (task.getParents().size() > 0){
                List<Task> parentsInOrderByName = new ArrayList<>(task.getParents());
                DotRenderer.sortTasks(parentsInOrderByName);
                for(Task parent : parentsInOrderByName){
                    output.append(DotRenderer.addDependency(parent, task));
                }
            }

        }

        // Append closing brace
        output.append(DotRenderer.closeGraph());
        writeDotFile(output.toString());
    }

    // This method takes the graph string and outputs a dot graph with the filename
    private void writeDotFile(String graph){
        try (PrintStream out = new PrintStream(new FileOutputStream(filename))) {
            out.print(graph);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

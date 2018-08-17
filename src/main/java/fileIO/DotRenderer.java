package fileIO;

import scheduleModel.IProcessor;
import taskModel.Task;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Helper class used to render DOT files
 */
public class DotRenderer {
    private static final String NEWLINE = String.format("%n");
    private static final String TAB = "\t";

    protected DotRenderer(){
    }

    public static String openGraph(String title) {
        return "digraph \"" +
                title +
                "\" {" +
                NEWLINE;
    }

    public static String addNode(Task task, int startTime, IProcessor processor){
        return TAB +
                task.getName() +
                TAB + TAB +
                "[Weight=" +
                task.getWeight() +
                ",Start=" +
                startTime +
                ",Processor=" +
                processor.toString() +
                "];" +
                NEWLINE;
    }

    public static String addDependency(Task parent, Task child){
        return TAB +
                parent.getName() +
                " -> " +
                child.getName() +
                TAB +
                "[Weight=" +
                parent.getChildLinkCost(child) +
                "];" +
                NEWLINE;
    }


    public static String closeGraph(){
        return "}";
    }

    public static void sortTasks(List<Task> tasks) {
        try {
            tasks.sort(Comparator.comparing(Task::getId));
        } catch (NumberFormatException e) {
            tasks.sort(Comparator.comparing(Task::getName));
        }
    }
}

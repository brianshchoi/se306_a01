package app;


import fileIO.DotGraph;
import fileIO.FileParser;
import javafx.beans.Observable;
import scheduleModel.ISchedule;
import scheduleModel.Schedule;
import taskModel.TaskModel;
import view.Visualizer;
import view.ganttChart.GanttChartScheduler;
import view.listeners.AlgorithmListener;
import view.listeners.AlgorithmObservable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;


/**
 * This class represents the Command Line Interface (CLI) of the application.
 * It extracts the arguments passed in at the command line and uses them appropriately.
 * The usage at command line is defined as follows:
 *
 * java -jar scheduler.jar INPUT.dot P [OPTION]
 * INPUT.dot is a task graph with integer weights in dot format
 * P is number of processors to scheduleModel the INPUT graph on
 *
 * Optional:
 * -p N use N cores for execution in parallel (default is sequential)
 * -v visualise the search
 * -o OUTPUT output file is named OUTPUT (default is INPUT-output.dot)
 */
public class CLI {
    // Set constants
    private static int MIN_ARGS = 2;
    private static int MAX_ARGS = 7;

    // Set defaults
    private static boolean visualisation = false;
    private static String outputFilename = null;
    private static int numOfProcessors = 1;
    private static int algorithmCores = 1;
    private static String inputFilename = null;
    private static final String USAGE =
            "java -jar scheduler.jar INPUT.dot P [OPTION]\n" +
            "INPUT.dot  a task graph with integer weights in dot format\n" +
            "P          number of processors to schedule the INPUT graph on\n\n" +
            "Optional:\n" +
            "-p N       use N cores for execution in parallel (default is sequential)\n" +
            "-v         visualise the search\n" +
            "-o OUTPUT  output file is named OUTPUT (default is input-OUTPUT.dot)";
    private static TaskModel taskModel;

    public static void main(String[] args) throws CloneNotSupportedException {
        List<String> argsList = Arrays.asList(args);

        // Checks that there is a valid number of arguments
        if (argsList.size() < MIN_ARGS){
            System.err.println("Not enough arguments.  See usage below:\n");
            System.out.println(USAGE);
            return;
        }
        else if (argsList.size() > MAX_ARGS){
            System.err.println("Too many arguments.  See usage below:\n");
            System.out.println(USAGE);
            return;
        }


        // Get mandatory arguments
        inputFilename = argsList.get(0);
        try {
            numOfProcessors = Integer.parseInt(argsList.get(1));
            if (numOfProcessors < 0) throw new NumberFormatException();
        } catch (NumberFormatException e){
            System.out.println("Not a valid integer for the number of processors. See usage below:\n");
            System.out.println(USAGE);
            return;
        }

        // Check for visualisation (off by default)
        if (argsList.contains("-v")) {
            visualisation = true;
        }

        // Check for number of cores to run algorithm on (1 by default)
        if (argsList.contains("-p")) {
            String N = argsList.get(argsList.indexOf("-p") + 1);
            try {
                algorithmCores = Integer.parseInt(N);
                if (algorithmCores < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("Not a valid integer for the number of algorithm cores.  See usage below:\n");
                System.out.println(USAGE);
                return;
            }
        }

        // Configure output filename
        if (argsList.contains("-o")) {
            outputFilename = argsList.get(argsList.indexOf("-o") + 1) + ".dot";
        } else {
            outputFilename = inputFilename.replace(".dot", "") + "-output.dot";
        }


        // Create file parser
        FileParser fileParser = null;
        try {
            fileParser = new FileParser(new File(inputFilename));
        } catch (FileNotFoundException e) {
            System.out.println("The file could not be found.");
            return;
        }

        // Parse the file
        taskModel = fileParser.getTaskModelFromFile();

        if (visualisation) {
            // Visualisation
            System.out.println("Starting visualizer...");
            new Thread(() -> Visualizer.launch(taskModel, new Schedule(numOfProcessors))).start();
        } else {
            visualizerReady(null);
        }
    }

    /**
     * This method is called by the Visualizer wants it has initialized, so that the appropriate visual
     * components can be then registered on the algorithm as listeners.
     * @param listeners
     */
    public static void visualizerReady(List<AlgorithmListener> listeners) {
        // Set algorithm
        IAlgorithm algorithm = new DFSAlgorithmFork(taskModel, numOfProcessors, algorithmCores);

        // Register listeners with algorithm
        if (visualisation) {
            for (AlgorithmListener listener: listeners) {
                ((AlgorithmObservable) algorithm).addAlgorithmListener(listener);
            }
        }

        // Get optimal schedule
        ISchedule schedule = algorithm.run();

        // Uncomment to run validator
        // new ScheduleValidator(schedule).validate(taskModel);

        //Write out to file
        DotGraph dotGraph = new DotGraph(outputFilename, taskModel.getGraphId(), schedule, taskModel);
        dotGraph.render();
        System.out.println(outputFilename + " has been saved.");
    }

    public static boolean isVisualisation() {
        return visualisation;
    }
}

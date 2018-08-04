package app;


import scheduleModel.*;
import taskModel.TaskModel;

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

    public static void main(String[] args) throws CloneNotSupportedException {
        List<String> argsList = Arrays.asList(args);

        // Checks that there is a valid number of arguments
        if (argsList.size() < MIN_ARGS){
            throw new IncorrectArgumentsException("Please enter at least 2 arguments");
        }
        else if (argsList.size() > MAX_ARGS){
            throw new IncorrectArgumentsException("There are too many arguments");
        }


        // Get mandatory arguments
        inputFilename = argsList.get(0);
        try {
            numOfProcessors = Integer.parseInt(argsList.get(1));
        } catch (NumberFormatException e){
            System.out.println("Not a valid integer for the number of processors");
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
            } catch (NumberFormatException e) {
                System.out.println("Not a valid integer for the number of algorithm cores");
            }
        }

        // Configure output filename
        if (argsList.contains("-o")) {
            outputFilename = argsList.get(argsList.indexOf("-o") + 1);
        } else {
            outputFilename = inputFilename.replace(".dot", "") + "-output.dot";
        }


        // Create file parser
        FileParser fileParser = null;
        try {
            fileParser = new FileParser(new File(System.getProperty("user.dir") + "/" + inputFilename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Parse the file
        TaskModel taskModel = fileParser.getTaskModelFromFile();

        // Get optimal schedule
        ISchedule schedule = new Algorithm(taskModel, numOfProcessors).run();
        schedule.debug();

        // Validate
        new ScheduleValidator(schedule).validate(taskModel);
    }
}

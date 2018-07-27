package app;

import model.TaskModel;
import java.util.Arrays;
import java.util.List;


/**
 * This class represents the Command Line Interface (CLI) of the application.
 * It extracts the arguments passed in at the command line and uses them appropriately.
 * The usage at command line is defined as follows:
 *
 * java -jar scheduler.jar INPUT.dot P [OPTION]
 * INPUT.dot is a task graph with integer weights in dot format
 * P is number of processors to schedule the INPUT graph on
 *
 * Optional:
 * -p N use N cores for execution in parallel (default is sequential)
 * -v visualise the search
 * -o OUTPUT output file is named OUTPUT (default is INPUT-output.dot)
 */
public class CLI {
    // Set defaults
    private static boolean visualisation = false;
    private static String outputFilename = null;
    private static int numOfProcessors = 1;
    private static int algorithmCores = 1;
    private static String inputFilename = null;

    public static void main(String[] args) {
        // TODO: 25/07/2018 convert args array to List<String> object using Arrays.asList
        List<String> argsList = Arrays.asList(args);


        // TODO: 25/07/2018 extract filename from the list object (first item)
        inputFilename = argsList.get(0);

        // TODO: 25/07/2018 extract number of processors from the list object (second item)
        numOfProcessors = Integer.parseInt(argsList.get(1));

        // TODO: 25/07/2018 check if list contains "-v" flag and set boolean field accordingly
        if (argsList.contains("-v")) {
            visualisation = true;
        }

        // TODO: 25/07/2018 check if list contains "-p" and if so, get the index of this String, add 1 to it, and lookup N using the resulting index

//        for (int i = 2; i < argsList.size(); i++) {
//            if (args[i].equals("-p")) {
//                try {
//                    algorithmCores = Integer.parseInt(args[i + 1]);
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//                i++;
//            } else if (args[i].equals("-o")) {
//                outputFilename = args[i+1] + ".dot";
//                i++;
//            }
//        }
        if (argsList.contains("-p")) {
            String N = argsList.get(argsList.indexOf("-p") + 1);
            // TODO: 25/07/2018 set algorithmCores appropriately if you need to
            try {
                algorithmCores = Integer.parseInt(N);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }


        // TODO: 25/07/2018 check if list contains "-o" and if so, do same thing as above (lookup indexOf("-o")+1 to get OUTPUT)

        if (argsList.contains("-o")) {
            outputFilename = argsList.get(argsList.indexOf("-o") + 1);
        } else {
            outputFilename = inputFilename.replace(".dot", "") + "-output.dot";
        }
        // TODO: 25/07/2018 instantiate FileParser and pass filename into constructor.
        FileParser fileParser = new FileParser(inputFilename);


        // TODO: 25/07/2018 set outputFilename accordingly if you need to


        // TODO: 25/07/2018 invoke appropriate method on FileParser object to get TaskModel object.  Store this in local variable
        TaskModel taskModel = fileParser.getTaskModelFromFile();
    }
}

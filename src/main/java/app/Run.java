package app;

import fileIO.FileParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/**
 * Convenience class used for testing algorithm without file IO
 */
public class Run {
    private static final String NODES_7 = "Nodes_7_OutTree.dot";
    private static final String NODES_8 = "Nodes_8_Random.dot";
    private static final String NODES_9 = "Nodes_9_SeriesParallel.dot";
    private static final String NODES_10 = "Nodes_10_Random.dot";
    private static final String NODES_11 = "Nodes_11_OutTree.dot";
    private static final String NODES_20 = "Nodes_20_Random.dot";
    private static final String NODES_FORK_JOIN = "2p_Fork_Join_Nodes_10_CCR_1.01_WeightType_Random.dot";
    private static final String NODES_FORK = "2p_Fork_Nodes_10_CCR_0.10_WeightType_Random.dot";

    private static final String FILENAME = NODES_FORK; // SET INPUT HERE
    private static final int NUM_PROCESSORS = 2; // SET PROCESSORS HERE
    private static final int NUM_CORES = 4; // SET PARALLELS HERE

    public  static void main(String[] args) throws FileNotFoundException, URISyntaxException, CloneNotSupportedException {
        // Start timing algorithm
        final double start = System.currentTimeMillis();

        //new DFSAlgorithm(new FileParser(new File(Run.class.getResource(FILENAME).toURI())).getTaskModelFromFile(), NUM_PROCESSORS).run().debug();

        new DFSAlgorithmFork(new FileParser(new File(Run.class.getResource(FILENAME).toURI())).getTaskModelFromFile(), NUM_PROCESSORS, NUM_CORES).run().debug();

        // Stop timing algorithm
        final double end = System.currentTimeMillis();
        System.out.println("Total execution time: " + (end - start)/1000 + " seconds");
    }
}

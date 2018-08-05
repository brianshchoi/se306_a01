package app;

import fileIO.FileParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class Run {
    private static final String NODES_7 = "Nodes_7_OutTree.dot";
    private static final String NODES_8 = "Nodes_8_Random.dot";
    private static final String NODES_9 = "Nodes_9_SeriesParallel.dot";
    private static final String NODES_10 = "Nodes_10_Random.dot";
    private static final String NODES_11 = "Nodes_11_OutTree.dot";

    private static final String FILENAME = NODES_7; // SET INPUT HERE
    private static final int NUM_PROCESSORS = 2;

    public  static void main(String[] args) throws FileNotFoundException, URISyntaxException, CloneNotSupportedException {
        new DFSAlgorithm(new FileParser(new File(Run.class.getResource(FILENAME).toURI())).getTaskModelFromFile(), NUM_PROCESSORS).run().debug();
    }
}

package app;

import fileIO.FileParser;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestAlgorithm {
    private Map<String, Integer> correctAnswers = new HashMap<String, Integer>() {{
        // Other inputs from Oliver Sinnen
        put("2p_Fork_Join_Nodes_10_CCR_1.01_WeightType_Random.dot", 59);
        put("2p_Fork_Nodes_10_CCR_0.10_WeightType_Random.dot", 300);
        put("2p_InTree-Balanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.dot", 222);
        put("2p_InTree-Unbalanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.dot", 344);
        put("2p_OutTree-Balanced-MaxBf-3_Nodes_10_CCR_0.10_WeightType_Random.dot", 253);

        // From Canvas
        put("Nodes_7_OutTree.dot", 28);
        put("Nodes_8_Random.dot", 581);
        put("Nodes_9_SeriesParallel.dot", 55);
        put("Nodes_10_Random.dot", 50);
        put("Nodes_11_OutTree.dot", 350);
        put("Nodes_20_Random.dot", 564);
    }};

    @Test
    public void testAll() throws URISyntaxException, FileNotFoundException {
        for (Map.Entry<String, Integer> entry: correctAnswers.entrySet()) {
            System.out.println("Testing " + entry.getKey() + "...");
            assertEquals((long) entry.getValue(), new DFSAlgorithm(new FileParser(new File(Run.class.getResource(entry.getKey()).toURI())).getTaskModelFromFile(), 2).run().getFinishTime());
        }
    }
}

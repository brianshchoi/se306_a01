package fileIO;

import fileIO.FileParser;
import scheduleModel.IProcessor;
import scheduleModel.ISchedule;
import scheduleModel.Schedule;
import taskModel.Task;
import taskModel.TaskModel;
import taskModel.TaskNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileParserTest {

    private FileParser fileParser;
    private static final String testFileName = "Nodes_7_OutTree.dot";
    private File file;
    private TaskModel taskModel;

    @Before
    public void setUp() throws URISyntaxException, FileNotFoundException {
        file = new File(this.getClass().getResource(testFileName).toURI());
        fileParser = new FileParser(file);
        taskModel = fileParser.getTaskModelFromFile();
    }

    /**
     * Test that all the nodes in the file are read
     */
    @Test
    public void testContainsAllNodes() {
        assertEquals(taskModel.getTasks().size(), 7);
    }

    /**
     * Test that all the edges in the file are read
     */
    @Test
    public void testContainsAllEdges() {
        List<Task> tasks = taskModel.getTasks();
        int inEdges = 0;
        int outEdges = 0;

        for (Task task: tasks) {
            inEdges += task.getParents().size();
            outEdges += task.getChildren().size();
        }

        assertEquals(inEdges, outEdges);
        assertEquals(inEdges, 6);
    }

    /**
     * Test that a given node exists (general case)
     */
    @Test
    public void testContainsNode() {
        try {
            taskModel.get("4");
        } catch (TaskNotFoundException e) {
            fail();
        }
    }

    /**
     * Test that a given edge exists (general case)
     */
    @Test
    public void testContainsEdge() {
        Task a = taskModel.get("0");
        Task b = taskModel.get("1");

        assertTrue(a.getChildren().contains(b));
        assertTrue(b.getParents().contains(a));
    }

    /**
     * Test that a node has a weight associated with it
     */
    @Test
    public void testNodeWeight() {
        assertEquals(taskModel.get("3").getWeight(), 6);
    }

    /**
     * Test that an edge has a cost associated with it
     */
    @Test
    public void testEdgeCost() {
        Task a = taskModel.get("0");
        Task b = taskModel.get("1");

        assertEquals(a.getChildLinkCost(b), 15);
        assertEquals(b.getParentLinkCost(a), 15);
    }

    /**
     * Ensure that edges are one-directional
     */
    @Test
    public void testEdgeDirection() {
        Task a = taskModel.get("0");
        Task b = taskModel.get("1");

        assertFalse(a.getParents().contains(b));
        assertFalse(b.getChildren().contains(a));
    }
}
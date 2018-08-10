package fileIO;

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
    private static final String TEST_FILE_NAME = "Nodes_7_OutTree.dot";
    private File file;
    private TaskModel taskModel;

    @Before
    public void setUp() throws URISyntaxException, FileNotFoundException {
        file = new File(this.getClass().getResource(TEST_FILE_NAME).toURI());
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

    @Test
    public void testBottomLevels() {
        Task t0, t1, t2, t3, t4, t5, t6;
        t0 = taskModel.get("0");
        t1 = taskModel.get("1");
        t2 = taskModel.get("2");
        t3 = taskModel.get("3");
        t4 = taskModel.get("4");
        t5 = taskModel.get("5");
        t6 = taskModel.get("6");

        assertEquals(18, t0.getBottomLevel());
        assertEquals(13, t1.getBottomLevel());
        assertEquals(5, t2.getBottomLevel());
        assertEquals(6, t3.getBottomLevel());
        assertEquals(4, t4.getBottomLevel());
        assertEquals(7, t5.getBottomLevel());
        assertEquals(7, t6.getBottomLevel());
    }
}
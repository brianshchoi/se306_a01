package fileIO;

import org.junit.Before;
import org.junit.Test;
import scheduleModel.IProcessor;
import scheduleModel.ISchedule;
import scheduleModel.Schedule;
import taskModel.Task;
import taskModel.TaskModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

public class DotGraphTest {
    private DotGraph dotGraph;

    @Before
    public void setup() {
        TaskModel model = new TaskModel("Test");

        Task a = new Task("a", 2);
        Task b = new Task("b", 3);
        Task c = new Task("c", 3);
        Task d = new Task("d", 2);

        model.addTask(a);
        model.addTask(b);
        model.addTask(c);
        model.addTask(d);

        model.addDependency(a,b,1);
        model.addDependency(a,c,2);
        model.addDependency(b,d,2);
        model.addDependency(b,d,1);

        ISchedule s = new Schedule(2);

        // Needed here to get processor object
        List<IProcessor> processors = s.getProcessors();
        IProcessor p1 = processors.get(0);
        IProcessor p2 = processors.get(1);

        s.schedule(a, p1, 0);
        s.schedule(b, p1, 2);
        s.schedule(c, p2, 4);
        s.schedule(d, p2, 7);

        dotGraph = new DotGraph("DotGraphTest.dot", model.getGraphId(), s, model);
    }

    @Test
    public void testWriteDotFile() throws FileNotFoundException {
        dotGraph.render();
        File testFile = new File(System.getProperty("user.dir") + "/" + "DotGraphTest.dot");
        FileParser fileParser = new FileParser(testFile);
        TaskModel taskModel = fileParser.getTaskModelFromFile();
        assertEquals(taskModel.getTasks().size(), 4);
        assertEquals(taskModel.getGraphId(), "outputTest");
        testFile.delete();
    }
}
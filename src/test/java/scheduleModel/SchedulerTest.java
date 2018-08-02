package scheduleModel;

import org.junit.Before;
import org.junit.Test;
import taskModel.Task;
import taskModel.TaskModel;

import java.security.spec.ECField;

import static org.junit.Assert.*;

public class SchedulerTest {

    private IScheduler scheduler;
    private Schedule schedule;

    private Task tZero;
    private Task tOne;
    private Task tTwo;
    private Task tThree;
    private Task tFour;

    private IProcessor processor0;
    private IProcessor processor1;



    @Before
    public void setup() {
        TaskModel taskModel = new TaskModel();

        // Create nodes
        tZero = new Task("0", 4);
        tOne = new Task("1", 2);
        tTwo = new Task("2", 2);
        tThree = new Task("3", 2);
        tFour = new Task("4", 5);
        Task tFive = new Task("5", 5);
        Task tSix = new Task("6", 10);

        // Add nodes to the task model
        taskModel.addTask(tZero);
        taskModel.addTask(tOne);
        taskModel.addTask(tTwo);
        taskModel.addTask(tThree);
        taskModel.addTask(tFour);
        taskModel.addTask(tFive);
        taskModel.addTask(tSix);

        // Add dependencies to the task model
        taskModel.addDependency(tZero, tOne, 1);
        taskModel.addDependency(tZero, tTwo, 1);
        taskModel.addDependency(tZero, tThree, 3);

        taskModel.addDependency(tOne, tFour, 1);
        taskModel.addDependency(tTwo, tFour, 2);
        taskModel.addDependency(tThree, tFive, 3);
        taskModel.addDependency(tFive, tSix, 5);
        taskModel.addDependency(tFour, tSix, 4);

        schedule = new Schedule(2);

        scheduler = new Scheduler();

        processor0 = schedule.getProcessors().get(0);
        processor1 = schedule.getProcessors().get(1);

    }

    @Test
    public void testEntryNodeFinishTime() {

        scheduler.schedule(tZero, processor0, schedule);
        assertEquals(4,processor0.getFinishTime());
        assertEquals(4, processor0.getFinishTimeOf(tZero));
    }

    @Test
    public void testProcessorContainsEntryNode(){
        scheduler.schedule(tZero, processor0, schedule);
        assertTrue(processor0.contains(tZero));
    }

    @Test
    public void testProcessorRemoveEntryNode(){
        scheduler.schedule(tZero, processor0, schedule);
        scheduler.remove(tZero,schedule);
        assertTrue(processor0.getTasks().isEmpty());
    }

    @Test
    public void testCentreNodeNoOverlap() {

        scheduler.schedule(tZero,processor0,schedule);
        scheduler.schedule(tOne, processor0, schedule);
        scheduler.schedule(tTwo, processor1, schedule);
        scheduler.schedule(tFour, processor1, schedule);
        assertEquals(12, processor1.getFinishTime());
    }

    @Test
    public void testCentreNodeOverlap() {
        scheduler.schedule(tZero,processor0,schedule);
        scheduler.schedule(tOne, processor0, schedule);
        scheduler.schedule(tTwo, processor1, schedule);
        scheduler.schedule(tThree, processor0, schedule);
        scheduler.schedule(tFour, processor1, schedule);
        assertEquals(12, processor1.getFinishTime());
    }

    @Test
    public void testCentreNodeOverlapReverse(){
        scheduler.schedule(tZero,processor0,schedule);
        scheduler.schedule(tOne, processor0, schedule);
        scheduler.schedule(tThree, processor0, schedule);
        scheduler.schedule(tTwo, processor1, schedule);
        scheduler.schedule(tFour, processor1, schedule);
        assertEquals(12, processor1.getFinishTime());
    }

    @Test
    public void testRemoveLastScheduledNode(){
        scheduler.schedule(tZero,processor0,schedule);
        scheduler.schedule(tOne, processor0, schedule);
        scheduler.schedule(tThree, processor0, schedule);
        scheduler.remove(tThree, schedule);
        assertFalse(processor0.contains(tThree));
    }

    @Test
    public void testRemoveCentreNode(){
        scheduler.schedule(tZero,processor0,schedule);
        scheduler.schedule(tOne, processor0, schedule);
        scheduler.schedule(tTwo, processor1, schedule);
        scheduler.schedule(tFour, processor1, schedule);
       // try {
            scheduler.remove(tOne, schedule);
      //  }
      //  catch {

      //  }

        //Should still contain the task since it wasn't the last one scheduled.
        assertEquals(true ,processor0.contains(tOne));
    }
}
package scheduleModel;

import org.junit.Before;
import org.junit.Test;
import taskModel.Task;
import taskModel.TaskModel;

import static org.junit.Assert.*;

public class SchedulerTest {

    private IScheduler scheduler;
    private Schedule schedule;

    private Task tZero;
    private Task tOne;
    private Task tTwo;
    private Task tThree;
    private Task tFour;



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

    }

    @Test
    public void testEntryNodeFinishTime() {

        IProcessor processor = schedule.getProcessors().get(0);
        scheduler.schedule(tZero, processor, schedule);
        assertEquals(4,processor.getFinishTime());
        assertEquals(4, processor.getFinishTimeOf(tZero));
    }

    @Test
    public void testProcessorContainsEntryNode(){
        IProcessor processor = schedule.getProcessors().get(0);
        scheduler.schedule(tZero, processor, schedule);
        assertTrue(processor.contains(tZero));
    }

    @Test
    public void testProcessorRemoveEntryNode(){
        IProcessor processor = schedule.getProcessors().get(0);
        scheduler.schedule(tZero, processor, schedule);
        scheduler.remove(tZero,schedule);
        assertTrue(processor.getTasks().isEmpty());
    }

    @Test
    public void testCentreNodeBeforeOverlap() {

        IProcessor processor0 = schedule.getProcessors().get(0);
        IProcessor processor1 = schedule.getProcessors().get(1);

        scheduler.schedule(tZero,processor0,schedule);
        assertEquals(4, processor0.getFinishTime());
        scheduler.schedule(tOne, processor0, schedule);
        assertEquals(6, processor0.getFinishTime());
        scheduler.schedule(tTwo, processor1, schedule);
        assertEquals(7, processor1.getFinishTime());
        scheduler.schedule(tFour, processor1, schedule);
        assertEquals(12, processor1.getFinishTime());
    }

    @Test
    public void testMiddleNodeAfterOverlap() {

        IProcessor processor0 = schedule.getProcessors().get(0);
        IProcessor processor1 = schedule.getProcessors().get(1);

        scheduler.schedule(tZero,processor0,schedule);
        assertEquals(4, processor0.getFinishTime());
        scheduler.schedule(tOne, processor0, schedule);
        assertEquals(6, processor0.getFinishTime());
        scheduler.schedule(tTwo, processor1, schedule);
        assertEquals(7, processor1.getFinishTime());
        scheduler.schedule(tThree, processor0, schedule);
        assertEquals(8, processor0.getFinishTime());
        scheduler.schedule(tFour, processor1, schedule);
        assertEquals(12, processor1.getFinishTime());
    }

    @Test
    public void testMiddleNodeAfterOverlapReverse(){
        IProcessor processor0 = schedule.getProcessors().get(0);
        IProcessor processor1 = schedule.getProcessors().get(1);

        scheduler.schedule(tZero,processor0,schedule);
        assertEquals(4, processor0.getFinishTime());
        scheduler.schedule(tOne, processor0, schedule);
        assertEquals(6, processor0.getFinishTime());
        scheduler.schedule(tThree, processor0, schedule);
        assertEquals(8, processor0.getFinishTime());
        scheduler.schedule(tTwo, processor1, schedule);
        assertEquals(7, processor1.getFinishTime());
        scheduler.schedule(tFour, processor1, schedule);
        assertEquals(12, processor1.getFinishTime());
    }

}
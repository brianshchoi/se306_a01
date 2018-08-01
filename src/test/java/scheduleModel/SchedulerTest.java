package scheduleModel;

import org.junit.Before;
import taskModel.Task;
import taskModel.TaskModel;

import static org.junit.Assert.*;

public class SchedulerTest {

    private IScheduler scheduler;


    @Before
    public void setup() {
        TaskModel taskModel = new TaskModel();

        // Create nodes
        Task tZero = new Task("0", 4);
        Task tOne = new Task("1", 2);
        Task tTwo = new Task("2", 2);
        Task tThree = new Task("3", 2);
        Task tFour = new Task("4", 5);
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
        taskModel.addDependency(tZero, tThree, 3);
        taskModel.addDependency(tZero, tTwo, 1);
        taskModel.addDependency(tOne, tFour, 1);
        taskModel.addDependency(tTwo, tFour, 2);
        taskModel.addDependency(tThree, tFive, 3);
        taskModel.addDependency(tFive, tSix, 5);
        taskModel.addDependency(tFour, tSix, 4);
    }

}
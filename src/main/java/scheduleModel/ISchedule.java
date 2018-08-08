package scheduleModel;

import taskModel.Task;
import taskModel.TaskModel;

import java.util.List;

public interface ISchedule {

    /**
     * Schedules task to IProcessor, starting from time
     * @param task
     * @param processor
     * @param time
     */
    void schedule(Task task, IProcessor processor, int time);

    /**
     * Returns time at which task finishes
     * @param task
     * @return
     */
    int getFinishTimeOf(Task task);

    int getStartTimeOf(Task task);

    /**
     * Removes task from schedule
     * @param task
     */
    void remove(Task task);

    /**
     * Gets finish time of schedule
     * @return
     */
    int getFinishTime();

    /**
     * Returns processor task is scheduled on
     * @param task
     * @return
     */
    IProcessor getProcessorOf(Task task);

    /**
     * Returns list of all processors
     * @return
     */
    List<IProcessor> getProcessors();

    /**
     * Returns list of all tasks which have been scheduled
     * @return
     */
    List<Task> getTasks();

    boolean contains(Task task);

    int getIdleTime();

    void debug();

    // Cost functions
    int f1();
    double f2(TaskModel taskModel);
    int f3(List<Task> freeTasks);
}

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

    /**
     * Returns true if this schedule contains the task.
     * @param task
     * @return
     */
    boolean contains(Task task);

    /**
     * Get the amount of free time between tasks on this schedule.
     * Any time available after the last tasks scheduled on each processor is ignored.
     * @return
     */
    int getIdleTime();

    /**
     * Give a rough printout of the schedule.
     */
    void debug();

    // Cost functions
    int f1();
    double f2(TaskModel taskModel);

    /**
     * Returns true if this schedule contains that processor.
     * @param processor
     * @return
     */
    boolean containsProcessor(IProcessor processor);
}

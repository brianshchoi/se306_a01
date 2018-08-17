package scheduleModel;

import taskModel.Task;

import java.util.List;

public interface IProcessor {

    /**
     * Checks if task is scheduled to this processor
     * @param task
     * @return true if the task is scheduled
     */
    boolean contains(Task task);

    /**
     * Removes task from this processor
     * @param task
     */
    void remove(Task task);

    /**
     * Returns finish time of last task on this processor
     * @return the finishing time
     */
    int getFinishTime();

    /**
     * Returns finish time of task on this processor
     * @param task
     * @return the finishing time of that task
     */
    int getFinishTimeOf(Task task);

    /**
     * Returns finish time of task on this processor
     * @param task
     * @return the start time of that task
     */
    int getStartTimeOf(Task task);

    /**
     * schedules task on processor at time
     * @param task
     * @param time
     */
    void schedule(Task task, int time);

    /**
     * Returns all tasks that have been scheduled on the processor
     * @return a List of tasks
     */
    List<Task> getTasks();

    /**
     * Get the ID of this processor.
     * @return an integer ID
     */
    int getId();

    /**
     * Get the free time between tasks on this processor.
     * @return an integer representing the idle time
     */
    int getIdleTime();

    /**
     * Check if this processor is equivalent to another processor.
     * Equivalent means same tasks scheduled at the same times
     * (does not have to be the same processor number).
     * @param processor
     * @return true if the processor is equivalent
     */
    boolean isEquivalent(Processor processor);
}

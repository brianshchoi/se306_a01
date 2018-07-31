package scheduleModel;

import taskModel.Task;

public interface Processor {

    /**
     * Checks if task is scheduled to this processor
     * @param task
     * @return
     */
    boolean contains(Task task);

    /**
     * Removes task from this processor
     * @param task
     */
    void remove(Task task);

    /**
     * Returns finish time of last task on this processor
     * @return
     */
    int getFinishTime();

    /**
     * Returns finish time of task on this processor
     * @param task
     * @return
     */
    int getFinishTimeOf(Task task);

    /**
     * schedules task on processor at time
     * @param task
     * @param time
     */
    void schedule(Task task, int time);
}

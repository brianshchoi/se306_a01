package scheduleModel;

import taskModel.Task;

public interface Scheduler {

    /**
     * Removes task from schedule
     * @param task
     */
    void remove(Task task);

    /**
     * schedules task on processor
     * @param task
     * @param processor
     */
    void schedule(Task task, Processor processor);
}

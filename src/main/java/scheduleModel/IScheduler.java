package scheduleModel;

import taskModel.Task;

public interface IScheduler {

    /**
     * Removes task from schedule
     * @param task
     */
    void remove(Task task);

    /**
     * schedules task on IProcessor
     * @param task
     * @param IProcessor
     */
    void schedule(Task task, IProcessor IProcessor);
}

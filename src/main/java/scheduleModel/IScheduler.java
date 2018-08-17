package scheduleModel;

import taskModel.Task;

public interface IScheduler {
    /**
     * Removes a particular task from a schedule
     * @param task
     * @param schedule
     */
    void remove(Task task, ISchedule schedule);

    /**
     * Given a task, a schedule, and a particular processor,
     * schedule that task on that processor on the schedule.
     * @param task
     * @param processor
     * @param schedule
     */
    void schedule(Task task, IProcessor processor, ISchedule schedule);

    /**
     * Get the earliest starting time of a given task on a given processor
     * for a given schedule.
     * @param task
     * @param processor
     * @param schedule
     * @return
     */
    int getEarliestStartTime(Task task, IProcessor processor, ISchedule schedule);
}

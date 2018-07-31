package scheduleModel;

import taskModel.Task;

public interface Scheduler {

    /**
     * Removes task from schedule
     * @param task
     */
    public void remove(Task task);

    /**
     * schedules task on processor
     * @param task
     * @param processor
     */
    public void schedule(Task task, Processor processor);

    /**
     * helper method of finding when task ca be scheduled on processor
     */
    //private int getScheduleTime(Task task, Processor processor);
}

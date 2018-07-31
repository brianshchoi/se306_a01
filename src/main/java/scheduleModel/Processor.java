package scheduleModel;

import taskModel.Task;

public interface Processor {

    /**
     * Checks if task is scheduled to this processor
     * @param task
     * @return
     */
    public boolean contains(Task task);

    /**
     * Removes task from this processor
     * @param task
     */
    public void remove(Task task);

    /**
     * Returns finish time of last task on this processor
     * @return
     */
    public int getFinishTime();

    /**
     * Returns finish time of task on this processor
     * @param task
     * @return
     */
    public int getFinishTimeOf(Task task);
}

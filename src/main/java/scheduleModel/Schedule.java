package scheduleModel;

import taskModel.Task;

import java.util.List;

public interface Schedule {

    /**
     * Schedules task to processor, starting from time
     * @param task
     * @param processor
     * @param time
     */
    public void schedule(Task task, Processor processor, int time);

    /**
     * Returns time at which task finishes
     * @param task
     * @return
     */
    public int getFinishTimeOf(Task task);

    /**
     * Removes task from schedule
     * @param task
     */
    public void remove(Task task);

    /**
     * Gets finish time of schedule
     * @return
     */
    public int getFinishTime();

    /**
     * Returns processor task is scheduled on
     * @param task
     * @return
     */
    public Processor getProcessorOf(Task task);

    /**
     * Returns list of all processors
     * @return
     */
    public List<Processor> getProcessors();
}

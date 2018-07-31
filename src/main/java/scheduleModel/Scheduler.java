package scheduleModel;

import taskModel.Task;

import java.util.*;

public class Scheduler implements IScheduler {
    private ISchedule schedule;
    private int maxTime = 0;

    public Scheduler(ISchedule schedule) {
        this.schedule = schedule;
    }

    // Removes a particular task from a schedule
    @Override
    public void remove(Task task) {
        schedule.remove(task);
    }

    @Override
    public void schedule(Task task, IProcessor iProcessor) {
        List<Task> parentsList = new ArrayList<>(task.getParents());
        List<IProcessor> processorList = schedule.getProcessors();

        // Loops through to find the processor that each parent belongs to
        for (Task parentTask : parentsList){
            for (IProcessor parentProcessor : processorList){
                if (parentProcessor.contains(parentTask)){
                    getTime(parentTask,task, parentProcessor, iProcessor);
                }
            }
        }
    }

    /**
     * Find the latest time you can schedule a particular task
     *
     * Checks if the parent task is in the same processor as the child.
     * Makes sure that the parent is the final task scheduled on that particular processor before obtaining the finish time.
     * If the parent task is not on the same processor as the child, then consider the communication costs
     *
     * @param parent
     * @param child
     * @param parentProcessor
     * @param childProcessor
     */
    private void getTime(Task parent, Task child, IProcessor parentProcessor, IProcessor childProcessor){
        if (parentProcessor.equals(childProcessor) && (parentProcessor.getFinishTimeOf(parent) == (parentProcessor.getFinishTime()))){
            if (parentProcessor.getFinishTime() > maxTime){
                maxTime = parentProcessor.getFinishTime();
            }
        }
        else if (!parentProcessor.equals(childProcessor) && (parentProcessor.getFinishTimeOf(parent) == (parentProcessor.getFinishTime()))){
            if ((parentProcessor.getFinishTime()+child.getParentLinkCost(parent)) > maxTime){
                maxTime = parentProcessor.getFinishTime();
            }
        }
    }

    public int getMaxTime() {
        return maxTime;
    }
}

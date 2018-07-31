package scheduleModel;

import taskModel.Task;

import java.util.*;

public class Scheduler implements IScheduler {
    private ISchedule schedule;
    private int maxTime = 0;

    public Scheduler(ISchedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public void remove(Task task) {
        schedule.remove(task);
    }

    @Override
    public void schedule(Task task, IProcessor iProcessor) {
        // TODO: 31/07/2018 Get all the tasks parents

        // TODO: 31/07/2018 initialize known max 

        // TODO: 31/07/2018 For each parent, if they are on a processor that is not this processor, then get the communication cost and add it to the time
        List<Task> parentsList = new ArrayList<>(task.getParents());
        List<IProcessor> processorList = schedule.getProcessors();


        for (Task parentTask : parentsList){
            for (IProcessor parentProcessor : processorList){
                if (parentProcessor.contains(parentTask)){
                    getTime(parentTask,task, parentProcessor, iProcessor);
                }
            }
        }
    }

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

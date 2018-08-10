package scheduleModel;

import taskModel.Task;

import java.util.*;

public class Scheduler implements IScheduler {

    // Removes a particular task from a schedule
    @Override
    public void remove(Task task, ISchedule schedule) {
        schedule.remove(task);
    }

    @Override
    public void schedule(Task task, IProcessor processor, ISchedule schedule) {
        schedule.schedule(task, processor, getEarliestStartTime(task, processor, schedule));
    }

    @Override
    public int getEarliestStartTime(Task task, IProcessor processor, ISchedule schedule) {
        int maxTime = 0;
        maxTime = processor.getFinishTime();

        List<Task> parentsList = new ArrayList<>(task.getParents());

        // Loops through each parent and checks it's finish time
        for (Task parentTask : parentsList) {
            IProcessor parentProcessor = schedule.getProcessorOf(parentTask);
            if (!parentProcessor.equals(processor)) {
                int parentTime = schedule.getFinishTimeOf(parentTask) + parentTask.getChildLinkCost(task);
                if (parentTime > maxTime) {
                    maxTime = parentTime;
                    System.out.println("time: " + task.getName() + " " + maxTime);
                }
            }
        }

        return maxTime;
    }
}

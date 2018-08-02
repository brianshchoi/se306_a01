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
    public void schedule(Task task, IProcessor iProcessor, ISchedule schedule) {
        int maxTime = 0;
        if (task.getParents().isEmpty()) {
            schedule.schedule(task, iProcessor, maxTime);
        } else {
            maxTime = iProcessor.getFinishTime();

            List<Task> parentsList = new ArrayList<>(task.getParents());

            // Loops through each parent and checks it's finish time
            for (Task parentTask : parentsList) {
                IProcessor parentProcessor = schedule.getProcessorOf(parentTask);
                if (!parentProcessor.equals(iProcessor)) {
                    int parentTime = schedule.getFinishTimeOf(parentTask) + parentTask.getChildLinkCost(task);
                    if (parentTime > maxTime){
                        maxTime = parentTime;
                    }
                }
            }
            schedule.schedule(task, iProcessor, maxTime);
        }
    }
}

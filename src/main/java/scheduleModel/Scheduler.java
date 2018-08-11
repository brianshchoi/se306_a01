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
        int earliestStartTime = 0;

        // First, find the best case scenario from costs incurred by parents, assuming no conflicting/overlapping tasks
        // on our processor.
        for (Task parent: task.getParents()) {
            // Check if parent is on our processor
            int timeAfterParentDone;
            if (processor.contains(parent)) {
                // If it is, then we can potentially start right after it.
                timeAfterParentDone = processor.getFinishTimeOf(parent);
            } else {
                // Otherwise, the parent must be on a different processor.
                // So we need to look at the finish time of the parent and add its link cost.
                timeAfterParentDone = schedule.getProcessorOf(parent).getFinishTimeOf(parent) + task.getParentLinkCost(parent);
            }

            // Now see if the potential start time which meets dependencies is worse than what we know.
            // If it is we need to update our starting time.
            if (timeAfterParentDone > earliestStartTime) {
                earliestStartTime = timeAfterParentDone;
            }
        }

        // Ok now we have found the best case scenario task time, but there might be a task scheduled there,
        // or there might be overlapping tasks.  We need to detect these and keep pushing the task down
        // until its happy.
        Task conflictingTask;
        while ((conflictingTask = getConflictingTask(task, processor, earliestStartTime)) != null) {
            earliestStartTime = suggestSaferStartTime(conflictingTask, processor);
        }

        return earliestStartTime;
    }

    private Task getConflictingTask(Task task, IProcessor processor, int attemptedTime) {

        // Go through each existing task on the processor and check for conflicts
        for (Task other: processor.getTasks()) {
            int otherStartTime = processor.getStartTimeOf(other);
            int otherFinishTime = processor.getFinishTimeOf(other);
            int myStartTime = attemptedTime;
            int myFinishTime = attemptedTime + task.getWeight();

            // Case 1:  end of other task overlaps onto the start of my one
            boolean startConflict = (otherStartTime < myStartTime) && (otherFinishTime > myStartTime);

            // Case 2:  end of my task overlaps onto beginning of other task
            boolean endConflict = (otherStartTime < myFinishTime) && (otherFinishTime > myStartTime);

            // Case 3:  my task completely covers another task
            boolean coveringConflict = (otherStartTime > myStartTime) && (otherFinishTime < myFinishTime);

            // Case 4:  another task complete covers my task
            boolean coveredConflict = (otherStartTime < myStartTime) && (otherFinishTime > myFinishTime);

            // There is a conflict if any of the four cases exist
            boolean conflictExists = startConflict || endConflict || coveringConflict || coveredConflict;

            if (conflictExists) {
                return other; // Return the other tassk if it conflicts
            }
        }
        // Otherwise everything's good so no conflict detected
        return null;
    }

    private int suggestSaferStartTime(Task conflictingTask, IProcessor processor) {
        return processor.getStartTimeOf(conflictingTask) + conflictingTask.getWeight();
    }
}

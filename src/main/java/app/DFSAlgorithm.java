package app;

import scheduleModel.*;
import taskModel.Task;
import taskModel.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class DFSAlgorithm implements IAlgorithm {

    private TaskModel taskModel;
    private IScheduler scheduler;
    private int numOfProcessors;

    private int bound = Integer.MAX_VALUE; // Stores current best finish time
    private ISchedule bestSchedule; // Stores current best schedule

    public DFSAlgorithm(TaskModel taskModel, int numOfProcessors) {
        this.taskModel = taskModel;
        this.numOfProcessors = numOfProcessors;
        scheduler = new Scheduler();
    }

    @Override
    public ISchedule run() {
        int depth = 0; // Stores depth of schedule
        Schedule schedule = new Schedule(numOfProcessors);
        List<Task> freeTasks = getFreeTasks(schedule, taskModel.getTasks());

        run(freeTasks, depth, schedule);

        return bestSchedule;
    }

    private void run(List<Task> freeTasks, int depth, ISchedule schedule) {
        if (!freeTasks.isEmpty()) {
            List<Task> scheduledTasks = schedule.getTasks(); // Store which tasks should be scheduled at each level

            for (Task currentTask: freeTasks){
                for (IProcessor currentProcessor: schedule.getProcessors()) {

                    // Remove extra tasks from schedule when backtracking
                    for (Task task : schedule.getTasks()) {
                        if (!scheduledTasks.contains(task)){
                            schedule.remove(task);
                        }
                    }

                    // Schedule task
                    scheduler.schedule(currentTask, currentProcessor, schedule);
                    depth++;

                    if (schedule.getFinishTime() < bound) {
                        int numTasks = taskModel.getTasks().size();

                        if (depth == numTasks) { // Update the best schedule
                            try {
                                bestSchedule = (ISchedule) ((Schedule) schedule).clone();
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            bound = bestSchedule.getFinishTime();
                        } else if (depth < numTasks) { // Keep building the schedule
                            // Set new list of free tasks
                            List<Task> newFreeTasks = getFreeTasks(schedule, taskModel.getTasks());
                            run(newFreeTasks, depth, schedule);
                        }
                    }
                    // Start backtracking
                    depth--;
                }
            }
        }
    }

    private List<Task> getFreeTasks(ISchedule schedule, List<Task> allTasks){
        List<Task> newFreeTasks = new ArrayList<>();

        // Create list of tasks which haven't been scheduled yet
        List<Task> scheduledTasks = schedule.getTasks();
        allTasks.removeAll(scheduledTasks);

        // Check if each unscheduled task's dependencies have been satisfied
        for (Task task: allTasks){
            if (scheduledTasks.containsAll(task.getParents())) {
                newFreeTasks.add(task);
            }
        }
        return newFreeTasks;
    }
}
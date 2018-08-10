package app;

import scheduleModel.*;
import taskModel.Task;
import taskModel.TaskModel;

import java.util.*;

public class DFSAlgorithm implements IAlgorithm {

    private TaskModel taskModel;
    private IScheduler scheduler;
    private int numOfProcessors;
    private int recursionLevel = 0;
    private Set<Schedule> allSchedules = new HashSet<>();

    private int bound = Integer.MAX_VALUE; // Stores current best finish time
    private ISchedule bestSchedule; // Stores current best schedule

    private int numBranches = 0;

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

        List<Task> previousTasks = new ArrayList<>();

        run(freeTasks, depth, schedule, previousTasks, null);
        System.out.println("Number of branches: " + numBranches);
        return bestSchedule;
    }

    private void run(List<Task> freeTasks, int depth, ISchedule schedule, List<Task> previousTasks, IProcessor previousProcessor) {
        recursionLevel++;

        if (!freeTasks.isEmpty()) {
            List<Task> scheduledTasks = schedule.getTasks(); // Store which tasks should be scheduled at each level

            for (Task currentTask : freeTasks) {

                // Remove tasks from list of previously tried tasks when backtracking
                previousTasks.retainAll(scheduledTasks);

                // If we have tried this task before, then
                // we do not need to schedule it on any processor
                // different to the processor of the previous task
                Set<IProcessor> processors = new HashSet<>();
                if (previousTasks.contains(currentTask)) {
                    processors.add(previousProcessor);
                } else {
                    processors.addAll(schedule.getProcessors());
                }

                // Update previous tasks
                previousTasks.add(currentTask);

                // Try scheduling task on each processor and add copy to set of unique schedules
                Set<ISchedule> schedules = new HashSet<>();
                for (IProcessor currentProcessor : processors) {
                    scheduler.schedule(currentTask, currentProcessor, schedule);
                    try {
                        schedules.add((ISchedule) ((Schedule) schedule).clone());
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    scheduler.remove(currentTask, schedule);
                }


                // Go through each of the unique created schedules at this level
                for (ISchedule currentSchedule : schedules) {
                    numBranches++;
                    schedule = currentSchedule;

                    // Update previousProcessor
                    previousProcessor = schedule.getProcessorOf(currentTask);
                    depth++;

                    // Check if bad schedule
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
                            //if(cost(schedule, newFreeTasks) < bound){
                                run(newFreeTasks, depth, schedule, previousTasks, previousProcessor);
                           // }
                        }
                    }
                    // Start backtracking
                    depth--;

                }
            }
        }
    }

    // Cost function
    private double cost(ISchedule schedule, List<Task> freeTasks) {
        TreeSet<Double> costFunctionOutputs = new TreeSet<>();
        costFunctionOutputs.add(((double) schedule.f1()));
        costFunctionOutputs.add(schedule.f2(taskModel));
        costFunctionOutputs.add((double) schedule.f3(freeTasks));

        return costFunctionOutputs.last();
    }

    private List<Task> getFreeTasks(ISchedule schedule, List<Task> allTasks) {
        List<Task> newFreeTasks = new ArrayList<>();

        // Create list of tasks which haven't been scheduled yet
        List<Task> scheduledTasks = schedule.getTasks();
        allTasks.removeAll(scheduledTasks);

        // Check if each unscheduled task's dependencies have been satisfied
        for (Task task : allTasks) {
            if (scheduledTasks.containsAll(task.getParents())) {
                newFreeTasks.add(task);
            }
        }
        return newFreeTasks;
    }
}
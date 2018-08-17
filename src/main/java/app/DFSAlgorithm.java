package app;

import scheduleModel.*;
import taskModel.Task;
import taskModel.TaskModel;
import view.listeners.AlgorithmListener;
import view.listeners.AlgorithmObservable;

import java.util.*;

/**
 * This was our initial algorithm we made before we parallelized it.
 * We have kept this class even though it is not currently used by the CLI.
 * This is because it is a convenient class to run tests on where it can be
 * isolated from the parallelization code (and is easier to modify for experimenting
 * with optimizations).
 */
public class DFSAlgorithm implements IAlgorithm, AlgorithmObservable {

    private TaskModel taskModel;
    private IScheduler scheduler;
    private int numOfProcessors;
    private int recursionLevel = 0; // debugging

    private int bound = Integer.MAX_VALUE; // Stores current best finish time
    private ISchedule bestSchedule; // Stores current best schedule

    private int numBranches = 0; // debugging
    private List<AlgorithmListener> listeners = new ArrayList<>();

    public DFSAlgorithm(TaskModel taskModel, int numOfProcessors) {
        this.taskModel = taskModel;
        this.numOfProcessors = numOfProcessors;
        scheduler = new Scheduler();
    }

    @Override
    public ISchedule run() {
        int depth = 0;
        // Create a blank schedule
        Schedule schedule = new Schedule(numOfProcessors);
        List<Task> freeTasks = getFreeTasks(schedule, taskModel.getTasks());
        Set<Task> previousTasks = new HashSet<>();

        // Make initial call
        run(freeTasks, depth, schedule, previousTasks, null);
        fire(EventType.ALGORTHIM_FINISHED);
        return bestSchedule;
    }

    @Override
    public ISchedule getBestSchedule() {
        return bestSchedule;
    }

    /**
     * The depth-first-search-branch-and-bound algorithm.
     * Recursively creates a schedule tree, with pruning, to find the optimal schedule
     * @param freeTasks
     * @param depth
     * @param schedule
     * @param cleanPreviousTasks
     * @param previousProcessor
     */
    private void run(List<Task> freeTasks, int depth, ISchedule schedule, Set<Task> cleanPreviousTasks, IProcessor previousProcessor) {
        recursionLevel++;

        if (!freeTasks.isEmpty()) {
            // Get the previous tasks above this layer in the recursion call
            Set<Task> previousTasks = new HashSet<>(cleanPreviousTasks);

            // Iterate through each eligible task
            for (Task currentTask : freeTasks) {

                // If the task we are currently scheduling
                // has been already scheduled in the past after scheduling
                // some other task in this layer, then this task has already been
                // tried on all the processors.  There is no point trying this task
                // on all the processors again - we should just stick them on the same
                // processor as that is the only thing that can give us something different.
                Set<IProcessor> processors = new HashSet<>();
                if (previousTasks.contains(currentTask)) {
                    processors.add(previousProcessor);
                } else {
                    processors.addAll(schedule.getProcessors()); // Otherwise we need to try it on all processors
                }

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

                // Remember that we have scheduled this task.
                // When we come back from the recursive call and go onto the
                // next currentTask, we know from previousTasks that THIS currentTask has been
                // tried on all the processors.
                previousTasks.add(currentTask);

                // Go through each of the unique created schedules at this level
                for (ISchedule currentSchedule : schedules) {
                    numBranches++;
                    fire(EventType.NUM_BRANCHES_CHANGED);
                    depth++;

                    // Check if bad schedule
                    if (cost(currentSchedule) < bound) {
                        int numTasks = taskModel.getTasks().size();
                        if (depth == numTasks) { // Update the best schedule
                            try {
                                bestSchedule = (ISchedule) ((Schedule) currentSchedule).clone();
                                if (CLI.isVisualisation()) fire(EventType.BEST_SCHEDULE_UPDATED);
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            bound = bestSchedule.getFinishTime();
                        } else if (depth < numTasks) { // Keep building the schedule
                            // Set new list of free tasks
                            List<Task> newFreeTasks = getFreeTasks(currentSchedule, taskModel.getTasks());
                            run(newFreeTasks, depth, currentSchedule, previousTasks, currentSchedule.getProcessorOf(currentTask));

                        }
                    }
                    // Start backtracking
                    depth--;
                }
            }
        }
    }

    // Cost function
    private double cost(ISchedule schedule) {
        TreeSet<Double> costFunctionOutputs = new TreeSet<>();
        costFunctionOutputs.add(((double) schedule.f1()));
        costFunctionOutputs.add(schedule.f2(taskModel));
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
        Collections.sort(newFreeTasks);
        return newFreeTasks;
    }

    @Override
    public void addAlgorithmListener(AlgorithmListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeAlgorithmListener(AlgorithmListener listener) {
        listeners.remove(listener);
    }

    // Fire an event to GUI listeners
    @Override
    public void fire(EventType eventType) {
        if (!CLI.isVisualisation()) return; // ignore if user didn't want visualization
        switch (eventType) {
            case BEST_SCHEDULE_UPDATED:
                for (AlgorithmListener listener : listeners) {
                    listener.bestScheduleUpdated(bestSchedule);
                }
                break;
            case ALGORTHIM_FINISHED:
                for (AlgorithmListener listener: listeners) {
                    listener.algorithmFinished();
                }
                break;
            case NUM_BRANCHES_CHANGED:
                for (AlgorithmListener listener: listeners) {
                    listener.numberOfBranchesChanged();
                }
                break;
        }
    }
}
package app;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import scheduleModel.*;
import taskModel.Task;
import taskModel.TaskModel;
import view.listeners.AlgorithmListener;
import view.listeners.AlgorithmObservable;

import java.util.*;

public class DFSAlgorithm implements IAlgorithm, AlgorithmObservable {

    private TaskModel taskModel;
    private IScheduler scheduler;
    private int numOfProcessors;
    private int recursionLevel = 0;

    private int bound = Integer.MAX_VALUE; // Stores current best finish time
    private ISchedule bestSchedule; // Stores current best schedule

    private int numBranches = 0;
    private List<AlgorithmListener> listeners = new ArrayList<>();

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
        Set<Task> pTasks = new HashSet<>();

        run(freeTasks, depth, schedule, pTasks, null);
        System.out.println("Number of branches: " + numBranches);
        fire(EventType.ALGORTHIM_FINISHED);
        return bestSchedule;
    }

    @Override
    public ISchedule getBestSchedule() {
        return bestSchedule;
    }

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
                    depth++;

                    // Check if bad schedule
                    if (currentSchedule.getFinishTime() < bound) {
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
                            if (cost(currentSchedule) < bound) {
                                run(newFreeTasks, depth, currentSchedule, previousTasks, currentSchedule.getProcessorOf(currentTask));
                            }
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

    @Override
    public void fire(EventType eventType) {
        if (!CLI.isVisualisation()) return;
        switch (eventType) {
            case BEST_SCHEDULE_UPDATED:
                for (AlgorithmListener listener: listeners) {
                    listener.bestScheduleUpdated(bestSchedule);
                }
                break;
            case ALGORTHIM_FINISHED:
                for (AlgorithmListener listener: listeners) {
                    listener.algorithmFinished();
                }
                break;
        }
    }
}
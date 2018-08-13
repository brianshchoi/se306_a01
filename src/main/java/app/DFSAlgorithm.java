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
        Set<Task> pTasks = new HashSet<>();

        run(freeTasks, depth, schedule, pTasks, null);
        System.out.println("Number of branches: " + numBranches);
        return bestSchedule;
    }

    private void run(List<Task> freeTasks, int depth, ISchedule schedule, Set<Task> cleanPTasks, IProcessor pProc) {
        recursionLevel++;

        if (!freeTasks.isEmpty()) {
            Set<Task> pTasks = new HashSet<>();
            pTasks.addAll(cleanPTasks);
            for (Task currentTask : freeTasks) {

                Set<IProcessor> processors = new HashSet<>();
                if (pTasks.contains(currentTask)) {
                    processors.add(pProc);
                    //System.out.println("true");
                } else {
                    processors.addAll(schedule.getProcessors());
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

                pTasks.add(currentTask);

                // Go through each of the unique created schedules at this level
                for (ISchedule currentSchedule : schedules) {
//                    List<Integer> finishTimes = new ArrayList<>();
//                    for (IProcessor processor: currentSchedule.getProcessors()){
//                        finishTimes.add(processor.getFinishTime());
//                    }






                    numBranches++;
                    depth++;


                    IProcessor newPProc = currentSchedule.getProcessorOf(currentTask);
                    // Check if bad schedule
                    if (currentSchedule.getFinishTime() < bound) {
                        int numTasks = taskModel.getTasks().size();
                        if (depth == numTasks) { // Update the best schedule
                            try {
                                bestSchedule = (ISchedule) ((Schedule) currentSchedule).clone();
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            bound = bestSchedule.getFinishTime();
                        } else if (depth < numTasks) { // Keep building the schedule
                            // Set new list of free tasks
                            List<Task> newFreeTasks = getFreeTasks(currentSchedule, taskModel.getTasks());
                            if (cost(currentSchedule) < bound) {

                                run(newFreeTasks, depth, currentSchedule, pTasks, newPProc);

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
}
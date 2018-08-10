package app;

import app.exception.UnimplmentedException;
import scheduleModel.*;
import taskModel.Task;
import taskModel.TaskModel;

import java.util.*;

public class DFSAlgorithm implements IAlgorithm {

    private TaskModel taskModel;
    private IScheduler scheduler;
    private int numOfProcessors;
    private boolean symmetric = true;
    private boolean firstTaskOnSymmetricScheduleDone = false;
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

        run(freeTasks, depth, schedule);
        System.out.println("Number of branches: " + numBranches);
        return bestSchedule;
    }

    private void run(List<Task> freeTasks, int depth, ISchedule schedule) {
        recursionLevel++;
        //System.out.println(recursionLevel);
        if (!freeTasks.isEmpty()) {
            List<Task> scheduledTasks = schedule.getTasks(); // Store which tasks should be scheduled at each level

            for (Task currentTask : freeTasks) {
                //System.out.println(currentTask.getName());

                for (Task task : schedule.getTasks()) {
                    if (!scheduledTasks.contains(task)) {
                        schedule.remove(task);
                    }
                }

                Set<ISchedule> schedules = new HashSet<>();
                for (IProcessor currentProcessor : schedule.getProcessors()) {
                    scheduler.schedule(currentTask, currentProcessor, schedule);
                    //schedule.debug();
                    try {
                        schedules.add((ISchedule) ((Schedule) schedule).clone());
                    } catch (CloneNotSupportedException e) {
                    }
                    scheduler.remove(currentTask,schedule);
                }

                for (ISchedule currentSchedule : schedules){
                    numBranches++;
                    // Remove extra tasks from schedule when backtracking
//                    for (Task task : schedule.getTasks()) {
//                        if (!scheduledTasks.contains(task)) {
//                            schedule.remove(task);
//                        }
//                    }
                    schedule=currentSchedule;
                    if (allSchedules.contains(schedule)) continue;
                    try {
                        allSchedules.add((Schedule) ((Schedule) schedule).clone());
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }

                    //scheduler.schedule(currentTask, currentProcessor, schedule);
                    depth++;

//                    // Remember that we have scheduled the first task on a blank schedule
//                    if (symmetric) {
//                        firstTaskOnSymmetricScheduleDone = true;
//                    }

                    if ((schedule.getFinishTime() < bound)) {
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
                            //if (schedule.getFinishTime() < cost(schedule, newFreeTasks)) { //Lucy wants to look at this
                                run(newFreeTasks, depth, schedule);
                            //}
                        }
                    }
                    // Start backtracking
                    depth--;
                }
            }
        }
        boolean backtracking = true;
    }


    private double cost(ISchedule schedule, List<Task> freeTasks) {
        TreeSet<Double> costFunctionOutputs = new TreeSet<>();
        costFunctionOutputs.add(((double) schedule.f1()));
        costFunctionOutputs.add(schedule.f2(taskModel));
        costFunctionOutputs.add((double)schedule.f3(freeTasks));

        return costFunctionOutputs.last();
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
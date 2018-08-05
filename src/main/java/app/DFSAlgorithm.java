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

    private int bound = Integer.MAX_VALUE; //stores current best finish time
    private ISchedule bestSchedule; //stores current best schedule

    public DFSAlgorithm(TaskModel taskModel, int numOfProcessors) {
        this.taskModel = taskModel;
        this.numOfProcessors = numOfProcessors;
        scheduler = new Scheduler();
    }

    @Override
    public ISchedule run() {

        int depth = 0; //stores depth of schedule
        Schedule schedule = new Schedule(numOfProcessors);
        List<Task> freeTasks = getFreeTasks(schedule, taskModel.getTasks());

        run(freeTasks, depth, schedule);

        return bestSchedule;
    }

    private void run(List<Task> freeTasks, int depth, ISchedule schedule) {
        if (!freeTasks.isEmpty()) {
            List<Task> scheduledTasks = schedule.getTasks(); //Store which tasks should be scheduled at each level

            for (int i = 0; i < freeTasks.size(); i++){
                for (int j = 0; j < schedule.getProcessors().size(); j++) {

                    //Remove extra tasks from schedule when backtracking
                    List<Task> tasks = schedule.getTasks();
                    for (Task task : tasks) {
                        if (!scheduledTasks.contains(task)){
                            schedule.remove(task);
                        }
                    }

                    //schedule task
                    Task cTask = freeTasks.get(i);
                    IProcessor cProc = schedule.getProcessors().get(j);
                    scheduler.schedule(cTask, cProc, schedule);

                    depth++;

                    //set new list of free tasks
                    List<Task> newFreeTasks = getFreeTasks(schedule, taskModel.getTasks());

                    if (schedule.getFinishTime() < bound) {
                        int numTasks = taskModel.getTasks().size();

                        if (depth == numTasks) { //update the best schedule
                            try {
                                bestSchedule = (ISchedule) ((Schedule) schedule).clone();
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            bound = bestSchedule.getFinishTime();
                        } else if (depth < numTasks) { //keep building the schedule
                            run(newFreeTasks, depth, schedule);
                        }
                    }
                    //start backtracking
                    depth--;
                }
            }
        }
    }

    private List<Task> getFreeTasks(ISchedule schedule, List<Task> allTasks){
        List<Task> newFreeTasks = new ArrayList<>();

        //create list of tasks which haven't been scheduled yet
        List<Task> scheduledTasks = schedule.getTasks();
        List<Task> unscheduledTasks = allTasks;
        unscheduledTasks.removeAll(scheduledTasks);

        //check if each unscheduled task's dependencies have been satisfied
        for (int k = 0; k < unscheduledTasks.size(); k++){
            Task task = unscheduledTasks.get(k);
            if (scheduledTasks.containsAll(task.getParents())){
                newFreeTasks.add(task);
            }
        }
        return newFreeTasks;
    }
}
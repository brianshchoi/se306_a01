package app;

import scheduleModel.*;
import taskModel.Task;
import taskModel.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class Algorithm {

    private TaskModel taskModel;
    private IScheduler scheduler;
    private int numOfProcessors;

    private int bound = Integer.MAX_VALUE;
    private ISchedule bestSchedule;

    public Algorithm(TaskModel taskModel, int numOfProcessors) {
        this.taskModel = taskModel;
        this.numOfProcessors = numOfProcessors;
        scheduler = new Scheduler();
    }

    public ISchedule run() {
        Task cTask = null;
        IProcessor cProc = null;
        int depth = 0;
        Schedule schedule = new Schedule(numOfProcessors);
        List<Task> freeTasks = getFreeTasks(schedule, taskModel.getTasks());
        List<Task> scheduledTasks = new ArrayList<>();

        run(cTask, cProc, freeTasks, depth, schedule);

        return bestSchedule;
    }

    private void run(Task cTask, IProcessor cProc, List<Task> freeTasks, int depth, ISchedule schedule) {
        boolean backtracking = false;
        if (!freeTasks.isEmpty()) {
            for (int i = 0; i < freeTasks.size(); i++){
                for (int j = 0; j < schedule.getProcessors().size(); j++) {
                    Task pTask = cTask;
                    IProcessor pProc = cProc;

                    cTask = freeTasks.get(i);
                    cProc = schedule.getProcessors().get(j);

                    //if backtracking, remove the most recently scheduled task
                    //error, sometimes you need to remove more than one task
                    if (pProc != null) {
                        if (backtracking || (j == 0 && pProc.equals(schedule.getProcessors().get(schedule.getProcessors().size() - 1)))) {
                            scheduler.remove(cTask, schedule);
                            backtracking = false;
                        }
                    }

                    scheduler.schedule(cTask, cProc, schedule);
                    depth++;

                    List<Task> newFreeTasks = getFreeTasks(schedule, taskModel.getTasks());

                    if (schedule.getFinishTime() < bound) {
                        int numTasks = taskModel.getTasks().size();
                        if (depth == numTasks) {
                            bestSchedule = schedule;
                            bound = bestSchedule.getFinishTime();
                        } else if (depth < numTasks) {
                            run(cTask, cProc, newFreeTasks, depth, schedule);
                        }
                    }
                    System.out.println(bound);
                    depth--;
                    backtracking = true;
                }
            }
        }
       // return bestSchedule;
    }

    private List<Task> getFreeTasks(ISchedule schedule, List<Task> allTasks){
        List<Task> newFreeTasks = new ArrayList<>();

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
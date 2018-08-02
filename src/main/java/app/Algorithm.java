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

    public Algorithm(TaskModel taskModel, int numOfProcessors) {
        this.taskModel = taskModel;
        this.numOfProcessors = numOfProcessors;
        scheduler = new Scheduler();
    }

    public ISchedule run() {
        int cTask = -1;
        int cProc = -1;
        int bound = Integer.MAX_VALUE;
        int depth = 0;
        Schedule schedule = new Schedule(numOfProcessors);
        Schedule bestSchedule = null;
        List<Task> freeTasks = getFreeTasks(schedule, taskModel.getTasks());

        return run(cTask, cProc, freeTasks, depth, schedule, bestSchedule, bound);
    }

    private ISchedule run(int cTask, int cProc, List<Task> freeTasks, int depth, ISchedule schedule, ISchedule bestSchedule, int bound) {
        if (!freeTasks.isEmpty()) {
            for (int i = 0; i < freeTasks.size(); i++){
                for (int j = 0; j < schedule.getProcessors().size(); j++) {
                    int pTask = cTask;
                    int pProc = cProc;

                    cTask = i;
                    cProc = j;

                    //if backtracking, remove the most recently scheduled task
                    if ((cTask == pTask) || ((cProc == 0) && (pProc == schedule.getProcessors().size() - 1))){
                        scheduler.remove(freeTasks.get(pTask), schedule);
                    }

                    //nothing is getting scheduled
                    scheduler.schedule(freeTasks.get(cTask), schedule.getProcessors().get(cProc), schedule);
                    depth++;

                    List<Task> newFreeTasks = getFreeTasks(schedule, taskModel.getTasks());

                    if (schedule.getFinishTime() < bound) {
                        int numTasks = taskModel.getTasks().size();
                        if (depth == numTasks) {
                            bestSchedule = schedule;
                            bound = bestSchedule.getFinishTime();
                        } else if (depth < numTasks) {
                            bestSchedule = run(cTask, cProc, newFreeTasks, depth, schedule, bestSchedule, bound);
                        }
                    }
                    depth--;
                }
            }
        }
        return bestSchedule;
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
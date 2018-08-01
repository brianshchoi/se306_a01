package app;

import scheduleModel.IProcessor;
import scheduleModel.ISchedule;
import scheduleModel.IScheduler;
import scheduleModel.Scheduler;
import taskModel.Task;
import taskModel.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class Algorithm {

    private TaskModel taskModel;
    private IScheduler scheduler;

    public Algorithm(TaskModel taskModel) {
        this.taskModel = taskModel;
        scheduler = new Scheduler();
    }

    public ISchedule run() {
        // TODO: 1/08/2018 insert initial values into run(....) 
        return run();
    }

    private ISchedule run(Task cTask, IProcessor cProc, Task pTask, IProcessor pProc, List<Task> freeTasks, int depth, ISchedule schedule, ISchedule bestSchedule, int bound) {
        if (!freeTasks.isEmpty()) {
            for (int i = 0; i < freeTasks.size(); i++){
                for (int j = 0; j < schedule.getProcessors().size(); j++) {
                    pTask = cTask;
                    pProc = cProc;
                    cTask = freeTasks.get(i);
                    cProc = schedule.getProcessors().get(j);

                    if (cTask.equals(pTask) || (j == 0 && pProc.equals(schedule.getProcessors().get(schedule.getProcessors().size() - 1)))){
                        scheduler.remove(pTask, schedule);
                    }

                    scheduler.schedule(cTask, cProc, schedule);
                    depth++;

                    List<Task> newFreeTasks = new ArrayList<>();
                    //somehow update newFreeTasks

                    if (schedule.getFinishTime() < bound) {
                        int numTasks = taskModel.getTasks().size();
                        if (depth == numTasks) {
                            bestSchedule = schedule;
                            bound = bestSchedule.getFinishTime();
                        } else if (depth < numTasks) {
                            bestSchedule = run(cTask, cProc, pTask, pProc, newFreeTasks, depth, schedule, bestSchedule, bound);
                        }
                    }
                    depth--;
                }
            }
        }
        return bestSchedule;
    }
}

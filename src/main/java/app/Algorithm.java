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

    public ISchedule run() throws CloneNotSupportedException {
        Task cTask = null;
        IProcessor cProc = null;
        int depth = 0;
        Schedule schedule = new Schedule(numOfProcessors);
        List<Task> freeTasks = getFreeTasks(schedule, taskModel.getTasks());
        List<Task> scheduledTasks = new ArrayList<>();

        run(cTask, cProc, freeTasks, depth, schedule);

        return bestSchedule;
    }

    private void run(Task cTask, IProcessor cProc, List<Task> freeTasks, int depth, ISchedule schedule) throws CloneNotSupportedException {
        if (!freeTasks.isEmpty()) {
            List<Task> scheduledTasks = schedule.getTasks();
            for (int i = 0; i < freeTasks.size(); i++){
                for (int j = 0; j < schedule.getProcessors().size(); j++) {
                    Task pTask = cTask;
                    IProcessor pProc = cProc;

                    cTask = freeTasks.get(i);
                    cProc = schedule.getProcessors().get(j);

                    List<Task> tasks = schedule.getTasks();

                    for (Task task : tasks) {
                        if (!scheduledTasks.contains(task)){
                            schedule.remove(task);
                        }
                    }



                    scheduler.schedule(cTask, cProc, schedule);
                    depth++;

                    List<Task> newFreeTasks = getFreeTasks(schedule, taskModel.getTasks());

                    if (schedule.getFinishTime() < bound) {
                        int numTasks = taskModel.getTasks().size();
                        if (depth == numTasks) {
                            bestSchedule = (ISchedule) ((Schedule) schedule).clone();
                            bound = bestSchedule.getFinishTime();
                        } else if (depth < numTasks) {
                            run(cTask, cProc, newFreeTasks, depth, schedule);
                        }
                    }
                    System.out.println(bestSchedule.getFinishTime());
                    depth--;
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
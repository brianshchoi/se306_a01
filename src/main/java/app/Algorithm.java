package app;

import scheduleModel.ISchedule;
import scheduleModel.IScheduler;
import scheduleModel.Schedule;
import scheduleModel.Scheduler;
import taskModel.TaskModel;

public class Algorithm {
    
    // TODO: 31/07/2018 Create fields here
    private TaskModel taskModel;
    private ISchedule schedule;
    private IScheduler scheduler;

    public Algorithm(TaskModel taskModel, int numOfProcessors) {
        this.taskModel = taskModel;
        schedule = new Schedule(numOfProcessors);
        scheduler = new Scheduler(schedule);
    }

    public ISchedule run() {
        // TODO: 31/07/2018 Implement algorithm


        throw new UnimplmentedException();
    }
}

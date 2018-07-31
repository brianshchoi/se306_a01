package scheduleModel;

import taskModel.Task;

public class Scheduler implements IScheduler {
    private ISchedule schedule;

    public Scheduler(ISchedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public void remove(Task task) {
        schedule.remove(task);
    }

    @Override
    public void schedule(Task task, IProcessor IProcessor) {
        // TODO: 31/07/2018 Get all the tasks parents

        // TODO: 31/07/2018 initialize known max 

        // TODO: 31/07/2018 For each parent, if they are on a processor that is not this processor, then get the communication cost and add it to the time

        
    }
}

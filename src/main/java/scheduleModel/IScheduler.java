package scheduleModel;

import taskModel.Task;

public interface IScheduler {

    // Removes a particular task from a schedule
    void remove(Task task, ISchedule schedule);

    void schedule(Task task, IProcessor iProcessor, ISchedule schedule);

    int getMaxTime();
}

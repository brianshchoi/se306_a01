package app;

import scheduleModel.IProcessor;
import scheduleModel.ISchedule;
import taskModel.Task;

import java.util.List;

public class Schedule implements ISchedule{

    private List<IProcessor> _processors;

    public Schedule(List<IProcessor> processors) {
        this._processors = processors;
    }

    @Override
    public void schedule(Task task, IProcessor processor, int time) {
        processor.schedule(task, time);
    }

    @Override
    public int getFinishTimeOf(Task task) {
        for (IProcessor p : this._processors) {
            if (p.contains(task)) {
                return p.getFinishTimeOf(task);
            }
        }

        throw new IncorrectArgumentsException("There are no processors which contain the task: " + task.getName());
    }

    @Override
    public void remove(Task task) {
        boolean taskRemoved = false;

        for (IProcessor p : this._processors) {
            if (p.contains(task)) {
                p.remove(task);
                taskRemoved = true;
                break;
            }
        }

        if (!taskRemoved){
            throw new IncorrectArgumentsException("Could not find the task: " + task.getName() + " in the schedule");
        }
    }

    @Override
    public int getFinishTime() {
        int maxFinishTime = -1;

        for (IProcessor p : _processors) {
            if (p.getFinishTime() > maxFinishTime){
                maxFinishTime = p.getFinishTime();
            }
        }

        if (maxFinishTime == -1){
            throw new IncorrectArgumentsException("Could not find a valid finishing time");
        }

        return maxFinishTime;
    }

    @Override
    public IProcessor getProcessorOf(Task task) {
        for (IProcessor p : this._processors) {
            if (p.contains(task)) {
                return p;
            }
        }

        throw new IncorrectArgumentsException("There are no processors which contain the task: " + task.getName());
    }

    @Override
    public List<IProcessor> getProcessors() {
        return this._processors;
    }
}

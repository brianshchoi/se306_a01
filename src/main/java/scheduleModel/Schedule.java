package scheduleModel;

import taskModel.Task;

import java.util.ArrayList;
import java.util.List;

public class Schedule implements ISchedule{

    private List<IProcessor> _processors = new ArrayList<>();

    public Schedule(int numOfProcessors) {
        for (int i = 0; i < numOfProcessors; i++) {
            _processors.add(new Processor());
        }
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

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        //get all tasks that have been scheduled in each processor
        for (int i =0; i < _processors.size(); i++){
            IProcessor processor = _processors.get(i);
            tasks.addAll(processor.getTasks());
        }
        return tasks;
    }
}

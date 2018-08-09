package scheduleModel;

import taskModel.Task;

import java.util.*;

public class Schedule implements ISchedule, Cloneable {

    private List<IProcessor> _processors = new ArrayList<>();
    private Map<Task, IProcessor> _tasksToProcessor = new HashMap<>();

    public Schedule(int numOfProcessors) {
        for (int i = 1; i <= numOfProcessors; i++) {
            _processors.add(new Processor(i));
        }
    }

    private Schedule() {} // for cloning

    @Override
    public Object clone() throws CloneNotSupportedException {
        Schedule schedule = new Schedule();

        for (IProcessor processor: _processors) {
            schedule._processors.add((IProcessor) ((Processor) processor).clone());
        }

        for (Task task: _tasksToProcessor.keySet()) {
            schedule._tasksToProcessor.put(task, (IProcessor) ((Processor)this._tasksToProcessor.get(task)).clone());
        }

        return schedule;
    }

    @Override
    public void schedule(Task task, IProcessor processor, int time) {
        processor.schedule(task, time);
        _tasksToProcessor.put(task, processor);
    }

    @Override
    public int getFinishTimeOf(Task task) {
        //get the processor the task is scheduled in
        IProcessor processor = _tasksToProcessor.get(task);

        if (processor != null){
            return processor.getFinishTimeOf(task);
        }
        throw new IncorrectArgumentsException("There are no processors which contain the task: " + task.getName());
    }

    @Override
    public int getStartTimeOf(Task task) {
        //get processor task is scheduled in
        IProcessor processor = _tasksToProcessor.get(task);

        if (processor != null){
            return processor.getStartTimeOf(task);
        }
        throw new IncorrectArgumentsException("There are no processors which contain the task: " + task.getName());
    }

    @Override
    public void remove(Task task) {
        boolean taskRemoved = false;

        //get processor task is scheduled in
        IProcessor processor = _tasksToProcessor.get(task);
        if (processor != null) {
            processor.remove(task);
            _tasksToProcessor.remove(task);
            taskRemoved = true;
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
        IProcessor processor = _tasksToProcessor.get(task);
        if (processor != null) {
            return processor;
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
        for (IProcessor processor: _processors){
            tasks.addAll(processor.getTasks());
        }
        return tasks;
    }

    @Override
    public boolean contains(Task task) {

        for (IProcessor processor: _processors) {
            if (processor.contains(task)) return true;
        }

        return false;
    }

    @Override
    public void debug() {
        for (IProcessor processor: _processors) {
            System.out.println("On processor " + processor.getId() + ":");
            List<Task> tasks = new ArrayList<>(processor.getTasks());
            Collections.sort(tasks);
            for (Task task: tasks) {
                System.out.println("Task " + task.getName() + " starts at time " + processor.getStartTimeOf(task) + ", "
                    + "finishes at time " + processor.getFinishTimeOf(task));
            }
        }
        System.out.println("The schedule has a makespan of " + getFinishTime());
    }
}

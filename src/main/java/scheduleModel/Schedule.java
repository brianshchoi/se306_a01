package scheduleModel;

import fileIO.DotRenderer;
import taskModel.Task;
import taskModel.TaskModel;

import java.util.*;

public class Schedule implements ISchedule, Cloneable {

    private List<IProcessor> _processors = new ArrayList<>();
  //  public Map<Task, IProcessor> _tasksToProcessor = new HashMap<>();

    public Schedule(int numOfProcessors) {
        // Create processors
        for (int i = 1; i <= numOfProcessors; i++) {
            _processors.add(new Processor(i));
        }
    }

    private Schedule() {} // for cloning

    // Create deep copy of Schedule
    @Override
    public Object clone() throws CloneNotSupportedException {
        Schedule schedule = new Schedule();
        // Clone the processors
        for (IProcessor processor: _processors) {
            schedule._processors.add((IProcessor) ((Processor) processor).clone());
        }
        return schedule;
    }

    @Override
    public void schedule(Task task, IProcessor processor, int time) {
        processor.schedule(task, time);
    }

    @Override
    public int getFinishTimeOf(Task task) {
        for (IProcessor processor: _processors){
            if (processor.contains(task)){
                return processor.getFinishTimeOf(task);
            }
        }
        throw new IncorrectArgumentsException("There are no processors which contain the task: " + task.getName());
    }

    @Override
    public int getStartTimeOf(Task task) {
        //get processor task is scheduled in
        for (IProcessor processor : _processors){
            if (processor.contains(task)){
                return processor.getStartTimeOf(task);
            }
        }

        throw new IncorrectArgumentsException("There are no processors which contain the task: " + task.getName());
    }

    @Override
    public void remove(Task task) {
        boolean taskRemoved = false;
        //get processor task is scheduled in
        for (IProcessor processor : _processors){
            if (processor.contains(task)){
                processor.remove(task);
                taskRemoved = true;
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
        for (IProcessor processor: _processors){
            if (processor.contains(task)) {
                return processor;
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
    public int getIdleTime() {
        int idleTime = 0;
        for (IProcessor processor: _processors) {
            idleTime += processor.getIdleTime();
        }

        return idleTime;
    }

    @Override
    public void debug() {
        for (IProcessor processor: _processors) {
            System.out.println("On processor " + processor.getId() + ":");
            List<Task> tasks = new ArrayList<>(processor.getTasks());
            DotRenderer.sortTasks(tasks);
            for (Task task: tasks) {
                System.out.println("Task " + task.getName() + " starts at time " + processor.getStartTimeOf(task) + ", "
                    + "finishes at time " + processor.getFinishTimeOf(task));
            }
        }
        System.out.println("The schedule has a makespan of " + getFinishTime());
    }

    // Maximum of start time + bottom level of any node
    @Override
    public int f1() {
        int maxBottomLevel = 0;
        for (Task task: getTasks()) {
            int f1NonMax = task.getBottomLevel() + getStartTimeOf(task);
            if (maxBottomLevel < f1NonMax) maxBottomLevel = f1NonMax;
        }
        return maxBottomLevel;
    }

    // Sum of weights of tasks + the idle time divided by the number of processors
    @Override
    public double f2(TaskModel taskModel) {
        return (taskModel.getComputationalLoad() + getIdleTime()) / (double) _processors.size();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Schedule)) return false;
        Schedule schedule = (Schedule) o;
        Set<IProcessor> thisProcessors = new HashSet<>(this._processors);
        Set<IProcessor> otherProcessors = new HashSet<>(schedule._processors);
        return thisProcessors.equals(otherProcessors);
    }

    @Override
    public boolean containsProcessor(IProcessor processor) {
        for (IProcessor myProcessor: _processors) {
            if (myProcessor.isEquivalent((Processor) processor)) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        List<Integer> processorHashCodes = new ArrayList<>();
        for (IProcessor processor: _processors) {
            processorHashCodes.add(processor.hashCode());
        }
        Collections.sort(processorHashCodes);
        return processorHashCodes.hashCode();
    }

}

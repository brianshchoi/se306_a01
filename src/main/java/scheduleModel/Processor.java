package scheduleModel;

import taskModel.Task;

import java.util.*;

public class Processor implements IProcessor, Cloneable {

    private int id;
    private Map<Task, Integer> taskMap = new TreeMap<>();
    private List<Task> tasks = new ArrayList<>();

    public Processor(int id) {
        this.id = id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Processor processor = new Processor(this.id);

        for (Task task: taskMap.keySet()) {
            processor.taskMap.put(task, taskMap.get(task));
        }

        processor.tasks.addAll(tasks);
        return processor;
    }

    @Override

    public boolean contains(Task task) {
        return taskMap.containsKey(task);
    }

    @Override
    public void remove(Task task) {
        taskMap.remove(task);
        tasks.remove(task);
    }

    @Override
    public int getFinishTime() {
        if (tasks.size() == 0) return 0;

        Task latestTaskScheduled = tasks.get(tasks.size() - 1);
        return taskMap.get(latestTaskScheduled) + latestTaskScheduled.getWeight();
    }

    @Override
    public int getFinishTimeOf(Task task) {
        return this.getStartTimeOf(task) + task.getWeight();
    }

    @Override
    public int getStartTimeOf(Task task) {
        return taskMap.get(task);
    }

    @Override
    public void schedule(Task task, int time) {
        taskMap.put(task, time);
        tasks.add(task);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(taskMap.keySet());
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }
}

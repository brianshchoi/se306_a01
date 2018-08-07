package scheduleModel;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import taskModel.Task;

import java.util.*;

public class Processor implements IProcessor, Cloneable {

    private int id;
    private Map<Task, Integer> taskMap = new HashMap<>();

    public Processor(int id) {
        this.id = id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Processor processor = new Processor(this.id);

        for (Task task: taskMap.keySet()) {
            processor.taskMap.put(task, taskMap.get(task));
        }

        return processor;
    }

    @Override

    public boolean contains(Task task) {
        return taskMap.containsKey(task);
    }

    @Override
    public void remove(Task task) {
        taskMap.remove(task);
    }

    @Override
    public int getFinishTime() {
        int maxFinishTime = 0;

        for (Map.Entry<Task, Integer> entry: taskMap.entrySet()) {
            int finishTime = entry.getValue() + entry.getKey().getWeight();
            if (finishTime > maxFinishTime) maxFinishTime = finishTime;
        }

        return maxFinishTime;
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

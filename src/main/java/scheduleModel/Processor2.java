package scheduleModel;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import taskModel.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Processor2 implements IProcessor {

    private int id;
    private BiMap<Integer, Task> taskBiMap = HashBiMap.create();
    private PriorityQueue<Integer> timeQueue = new PriorityQueue<>();

    public Processor2(int id) {
        this.id = id;
    }

    @Override
    public boolean contains(Task task) {
        return taskBiMap.containsValue(task);
    }

    @Override
    public void remove(Task task) {
        taskBiMap.inverse().remove(task);
    }

    @Override
    public int getFinishTime() {
        return timeQueue.peek() + taskBiMap.get(timeQueue.peek()).getWeight();
    }

    @Override
    public int getFinishTimeOf(Task task) {
        return this.getStartTimeOf(task) + task.getWeight();
    }

    @Override
    public int getStartTimeOf(Task task) {
        return taskBiMap.inverse().get(task);
    }

    @Override
    public void schedule(Task task, int time) {
        taskBiMap.put(time, task);
        timeQueue.add(time);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(taskBiMap.values());
    }

    @Override
    public int getId() {
        return id;
    }
}

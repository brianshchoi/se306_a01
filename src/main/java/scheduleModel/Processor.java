package scheduleModel;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import taskModel.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Processor implements IProcessor, Cloneable {

    private int id;
    private BiMap<Integer, Task> taskBiMap = HashBiMap.create();
    private PriorityQueue<Integer> startTimes = new PriorityQueue<>();

    public Processor(int id) {
        this.id = id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Processor processor = new Processor(this.id);

        for (Integer startTime: taskBiMap.keySet()) {
            processor.taskBiMap.put(startTime, taskBiMap.get(startTime));
        }

        return processor;
    }

    @Override
    public boolean contains(Task task) {
        return taskBiMap.containsValue(task);
    }

    @Override
    public void remove(Task task) {
        startTimes.remove(taskBiMap.inverse().get(task));
        taskBiMap.inverse().remove(task);
    }

    @Override
    public int getFinishTime() {
        if (startTimes.peek() == null) return 0;
        Task lastTask = taskBiMap.get(startTimes.peek());
        return startTimes.peek() + lastTask.getWeight();
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
        startTimes.add(time);
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

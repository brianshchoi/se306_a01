package scheduleModel;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import taskModel.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Processor implements IProcessor, Cloneable {

    private int id;
    private BiMap<Integer, Task> taskBiMap = HashBiMap.create();

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
        taskBiMap.inverse().remove(task);
    }

    @Override
    public int getFinishTime() {
        List<Integer> startTimes = new ArrayList<>(taskBiMap.keySet());
        if (startTimes.size() == 0) return 0;
        Collections.sort(startTimes);
        int startTime = startTimes.get(startTimes.size() - 1) ;
        return  startTime + taskBiMap.get(startTime).getWeight();
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
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(taskBiMap.values());
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

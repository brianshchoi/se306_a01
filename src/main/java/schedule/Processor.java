package schedule;

import model.Task;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Processor {
    private Map<Integer, Task> timeTaskMap = new HashMap<>();

    public void addAtTime(int time, Task task) {
        timeTaskMap.put(time, task);
    }

    public int getMakespan() {
        return getLatestStartingTime() + getLatestTask().getWeight();
    }

    public Task getLatestTask() {
        return timeTaskMap.get(getLatestStartingTime());
    }

    private int getLatestStartingTime() {
        Set<Integer> times = timeTaskMap.keySet();
        return Collections.max(times);
    }
}

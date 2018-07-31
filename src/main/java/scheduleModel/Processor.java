package scheduleModel;

import taskModel.Task;
import taskModel.TaskNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class Processor implements IProcessor {

    private Map<Integer, Task> tasksAtTime = new HashMap<>();

    @Override
    public boolean contains(Task task) {
        return tasksAtTime.containsValue(task);
    }

    @Override
    public void remove(Task task) {
        for (int k : tasksAtTime.keySet()){
            if (tasksAtTime.get(k).equals(task)){
                tasksAtTime.remove(k);
            }
        }
    }

    @Override
    public int getFinishTime() {
        // assuming Map may not be in order
        int max = 0;
        for (int k : tasksAtTime.keySet()){
            if(k > max){
                max = k;
            }
        }
        int lastTaskTime = tasksAtTime.get(max).getWeight();
        return max + lastTaskTime;
    }

    @Override
    public int getFinishTimeOf(Task task) {
        for (int k : tasksAtTime.keySet()){
            if (tasksAtTime.get(k).equals(task)){
                return k + task.getWeight();
            }
        }
        throw new TaskNotFoundException();
    }

    @Override
    public void schedule(Task task, int time) {
        tasksAtTime.put(time, task);
    }
}

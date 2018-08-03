package scheduleModel;

import taskModel.Task;
import taskModel.TaskNotFoundException;

import java.util.*;

public class Processor implements IProcessor, Cloneable {

    private Map<Integer, Task> tasksAtTime = new TreeMap<>();

    @Override
    public Object clone() throws CloneNotSupportedException {
        Processor processor = new Processor();

        for (Map.Entry<Integer, Task> entry: tasksAtTime.entrySet()) {
            processor.tasksAtTime.put(entry.getKey(), entry.getValue());
        }

        return processor;
    }

    @Override
    public boolean contains(Task task) {
        return tasksAtTime.containsValue(task);
    }

    //will throw concurrentModificationException
    //you can't edit a list while iterating through it
    @Override
    public void remove(Task task) {
        Integer key = -1;
        for (Map.Entry<Integer, Task> entry : tasksAtTime.entrySet()){
            if (task.equals(entry.getValue())){
                key = entry.getKey();
            }
        }
        tasksAtTime.remove(key);
    }

    @Override
    public int getFinishTime() {
        //accounts for when processor is empty
        if(tasksAtTime.isEmpty()){
            return 0;
        }
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
    public int getStartTimeOf(Task task) {
        for (int k : tasksAtTime.keySet()){
            if (tasksAtTime.get(k).equals(task)){
                return k;
            }
        }
        throw new TaskNotFoundException();
    }

    @Override
    public void schedule(Task task, int time) {
        tasksAtTime.put(time, task);
    }

    @Override
    public List<Task> getTasks(){
        List<Task> listOfTasks = new ArrayList<>(tasksAtTime.values());
        return listOfTasks;
    }
}

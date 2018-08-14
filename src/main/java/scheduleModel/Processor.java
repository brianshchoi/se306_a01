package scheduleModel;

import taskModel.Task;

import java.util.*;

public class Processor implements IProcessor, Cloneable {

    private int id;
    private Map<Task, Integer> taskMap = new HashMap<>();
    private List<Task> tasks = new ArrayList<>();
    private int allocatedTime = 0;

    public Processor(int id) {
        this.id = id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Processor processor = new Processor(this.id);
        processor.taskMap = new HashMap<>(taskMap);
        processor.allocatedTime = allocatedTime;
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
        allocatedTime -= task.getWeight();
    }

    public Map.Entry<Task, Integer> getLatestTask(){
        Map.Entry<Task, Integer> latestTask = taskMap.entrySet().iterator().next();

        for (Map.Entry<Task, Integer> entry: taskMap.entrySet()) {
            int taskFinishTime = entry.getValue() + entry.getKey().getWeight();
            int latestTaskFinish = latestTask.getValue() + latestTask.getKey().getWeight();

            if (taskFinishTime > latestTaskFinish) {
                latestTask = entry;
            }
        }

        return latestTask;
    }

    @Override
    public int getFinishTime() {
        int finishTime = 0;
        for (Map.Entry<Task, Integer> entry: taskMap.entrySet()) {
            int taskFinishTime = entry.getValue() + entry.getKey().getWeight();
            if (taskFinishTime > finishTime) finishTime = taskFinishTime;
        }

        return finishTime;
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
        allocatedTime += task.getWeight();
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
    public int getIdleTime() {
        return getFinishTime() - allocatedTime;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    @Override
    public boolean isEquivalent(Processor processor) {
        if (processor.taskMap.size() != taskMap.size()) return false;
        for (Map.Entry<Task, Integer> entry: taskMap.entrySet()) {
            if (!(processor.taskMap.containsKey(entry.getKey()))) return false;
            if (entry.getValue().intValue() != processor.taskMap.get(entry.getKey()).intValue()) return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof Processor)){
            return false;
        } else {
            Processor processor = (Processor) object;
            Map<Task, Integer> otherTaskMap = processor.taskMap;
            if (taskMap.keySet().equals(otherTaskMap.keySet())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Task, Integer> entry : taskMap.entrySet()){
            sb.append(entry.getKey().getName());
            sb.append(entry.getValue());
        }
        return sb.toString().hashCode();
    }
}

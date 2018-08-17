package taskModel;

import java.util.*;

/**
 * Class that stores all the tasks and provides
 * method for creating dependencies.
 */
public class TaskModel {
    private Map<String, Task> tasks = new HashMap<>();
    private String graphId;
    private int computationalLoad = 0; // Total weight of all tasks

    public TaskModel(String graphId) {
        this.graphId = graphId;
    }

    /**
     * Add a task to the TaskModel
     * @param task
     */
    public void addTask(Task task) {
        tasks.put(task.getName(), task);
        computationalLoad += task.getWeight();
    }

    /**
     * Get task from the model
     * @param taskName
     * @return
     */
    public Task get(String taskName) {
        if (!tasks.containsKey(taskName)) throw new TaskNotFoundException();
        return tasks.get(taskName);
    }

    /**
     * Add a dependency (communication cost) between two task nodes
     * @param parent
     * @param child
     * @param cost
     */
    public void addDependency(Task parent, Task child, int cost) {
        parent.insertLinkToChild(child, cost);
        child.insertLinkToParent(parent, cost);
    }

    /**
     * Get all the tasks in the model
     * @return
     */
    public List<Task> getTasks() {
        List<Task> output = new ArrayList<>();
        for (Task task: tasks.values()) {
            output.add(task);
        }
        return output;
    }

    public String getGraphId() {
        return graphId;
    }

    public int getTaskModelSize(){
        return tasks.size();
    }

    /**
     * Compute the bottom levels recursively
     */
    public void computeBottomLevels(Task task) {
        if (task.getChildren().size() == 0) {
            task.setBottomLevel(task.getWeight());
        } else {
            PriorityQueue<Task> priorityQueue = new PriorityQueue<>();
            for (Task child: task.getChildren()) {
                computeBottomLevels(child);
                priorityQueue.add(child);
            }
            task.setBottomLevel(priorityQueue.peek().getBottomLevel() + task.getWeight());
        }
    }

    public int getComputationalLoad() {
        return computationalLoad;
    }
}

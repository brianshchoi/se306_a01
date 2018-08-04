package taskModel;

import java.util.*;

/**
 * Class that stores all the tasks and provides
 * method for creating dependencies.
 */
public class TaskModel {
    private Map<String, Task> tasks = new HashMap<>();
    private Set<Task> entryTasks = new HashSet<>();
    private String graphId;

    public TaskModel(String graphId) {
        this.graphId = graphId;
    }

    /**
     * Add a task to the TaskModel
     * @param task
     */
    public void addTask(Task task) {
        tasks.put(task.getName(), task);
        entryTasks.add(task);
    }

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
        entryTasks.remove(child);  // This child node has parents so definitely cannot be an entry task
    }

    /**
     * Get the tasks which have no dependencies.
     * @return
     */
    public Set<Task> getEntryTasks() {
        return entryTasks;
    }

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
}

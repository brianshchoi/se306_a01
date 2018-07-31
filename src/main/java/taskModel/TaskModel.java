package taskModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class that stores all the tasks and provides
 * method for creating dependencies.
 */
public class TaskModel {
    private Map<String, Task> tasks = new HashMap<>();
    private Set<Task> entryTasks = new HashSet<>();

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

    public Set<Task> getTasks() {
        Set<Task> output = new HashSet<>();

        for (Task task: tasks.values()) {
            output.add(task);
        }

        return output;
    }
}

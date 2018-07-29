package model;

import java.util.HashSet;
import java.util.Set;

/**
 * Class that stores all the tasks and provides
 * method for creating dependencies.
 */
public class TaskModel {
    private Set<Task> tasks = new HashSet<>();
    private Set<Task> entryTasks = new HashSet<>();

    /**
     * Add a task to the TaskModel
     * @param task
     */
    public void addTask(Task task) {
        tasks.add(task);
        entryTasks.add(task);
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
        return tasks;
    }
}

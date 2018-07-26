package model;

import java.util.HashSet;
import java.util.Set;

/**
 * Singleton class that stores all the tasks and provides
 * method for creating dependencies.
 */
public class TaskModel {
    private static TaskModel instance = null;
    private Set<Task> tasks = new HashSet<>();
    private Set<Task> entryTasks = new HashSet<>();

    private TaskModel() {}

    /**
     * Get singleton instance
     * @return
     */
    public static TaskModel getInstance() {
        if (instance == null) {
            instance = new TaskModel();
        }

        return instance;
    }

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
}

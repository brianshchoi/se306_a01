package model;

import java.util.HashSet;
import java.util.Set;

public class TaskModel {
    private static TaskModel instance = null;
    private Set<Task> tasks = new HashSet<>();
    private Set<Task> entryTasks = new HashSet<>();

    private TaskModel() {}

    public  static TaskModel getInstance() {
        if (instance == null) {
            instance = new TaskModel();
        }

        return instance;
    }

    public void addTask(Task task) {
        tasks.add(task);
        entryTasks.add(task);
    }

    public void addDependency(Task parent, Task child, int cost) {
        parent.insertLinkToChild(child, cost);
        child.insertLinkToParent(parent, cost);
        entryTasks.remove(child);
    }

    public Set<Task> getEntryTasks() {
        return entryTasks;
    }
}

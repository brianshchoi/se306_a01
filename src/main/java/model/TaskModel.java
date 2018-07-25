package model;

import java.util.HashSet;
import java.util.Set;

public class TaskModel {
    private TaskModel instance = null;
    private Set<Task> tasks = new HashSet<>();

    private TaskModel() {}

    public  TaskModel getInstance() {
        if (instance == null) {
            instance = new TaskModel();
        }

        return instance;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
}

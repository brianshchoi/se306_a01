package taskModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A Task class represents a node in the task graph.  It is concerned with
 * packaging up task-relevant information such as the time (weight) of the task,
 * the task name (e.g. "A", "B") and the parent tasks and children tasks.
 * Parent tasks are tasks that this task depends on, and children tasks are
 * tasks that depend on this task.
 */
public class Task implements  Comparable<Task> {
    private Map<Task, Integer> parents = new HashMap<>();
    private Map<Task, Integer> children = new HashMap<>();

    private int weight;
    private String name;

    public Task(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    /**
     * Returns the Weight of the Task as int
     * @return int weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Returns the name of the task
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Maps parent task and cost from the parent together
     * @param parent
     * @param cost
     */
    void insertLinkToParent(Task parent, int cost) {
        this.parents.put(parent, cost);
    }

    /**
     * Maps child task and cost to the child together
     * @param child
     * @param cost
     */
    void insertLinkToChild(Task child, int cost) {
        this.children.put(child, cost);
    }

    /**
     * Returns a set of children tasks
     * @return Set<Task>
     */
    public Set<Task> getChildren() {
        return this.children.keySet();
    }

    /**
     * Returns a set of parent tasks
     * @return Set<Task>
     */
    public Set<Task> getParents() {
        return this.parents.keySet();
    }

    /**
     * Given a parent task, retrieve the link cost from that parent task
     * @param parent
     * @return
     */
    public int getParentLinkCost(Task parent) {
        return parents.get(parent);
    }

    /**
     * Given a child task, retrieve the link cost to that child task
     * @param child
     * @return
     */
    public int getChildLinkCost(Task child) {
        return children.get(child);
    }

    /**
     * Get name of task
     * @return
     */
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(Task task) {
        return this.name.compareTo(task.name);
    }
}

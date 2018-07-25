package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Task {
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
     * Maps parent task and cost to the parent together
     * @param parent
     * @param cost
     */
    public void insertLinkToParent(Task parent, int cost) {
        this.parents.put(parent, cost);
    }

    /**
     * Maps child task and cost to the child together
     * @param child
     * @param cost
     */
    public void insertLinkToChild(Task child, int cost) {
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

    public int getParentLinkCost(Task parent) {
        return parents.get(parent);
    }

    public int getChildLinkCost(Task child) {
        return children.get(child);
    }

    @Override
    public String toString() {
        return this.name;
    }
}

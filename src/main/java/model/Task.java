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

    public int getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public void insertLinkToParent(Task parent, int cost) {
        this.parents.put(parent, cost);
    }

    public void insertLinkToChild(Task child, int cost) {
        this.children.put(child, cost);
    }

    public Set<Task> getChildren() {
        return this.children.keySet();
    }

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

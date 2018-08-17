package taskModel;

import java.util.*;

/**
 * A Task class represents a node in the task graph.  It is concerned with
 * packaging up task-relevant information such as the time (weight) of the task,
 * the task name (e.g. "A", "B") and the parent tasks and childrenCosts tasks.
 * Parent tasks are tasks that this task depends on, and childrenCosts tasks are
 * tasks that depend on this task.
 */
public class Task implements  Comparable<Task> {
    private Map<Task, Integer> parentCosts = new HashMap<>();
    private Map<Task, Integer> childrenCosts = new HashMap<>();

    private int weight;
    private String name;
    private int bottomLevel;

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
        this.parentCosts.put(parent, cost);
    }

    /**
     * Maps child task and cost to the child together
     * @param child
     * @param cost
     */
    void insertLinkToChild(Task child, int cost) {
        this.childrenCosts.put(child, cost);
    }

    /**
     * Returns a set of childrenCosts tasks
     *
     * @return Set<Task>
     */
    public Set<Task> getChildren() {
        return this.childrenCosts.keySet();
    }

    /**
     * Returns a set of parent tasks
     * @return Set<Task>
     */
    public Set<Task> getParents() {
        return this.parentCosts.keySet();
    }

    /**
     * Given a parent task, retrieve the link cost from that parent task
     * @param parent
     * @return
     */
    public int getParentLinkCost(Task parent) {
        return parentCosts.get(parent);
    }

    /**
     * Given a child task, retrieve the link cost to that child task
     * @param child
     * @return
     */
    public int getChildLinkCost(Task child) {
        return childrenCosts.get(child);
    }

    /**
     * Get name of task
     * @return
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Compare tasks by their bottom levels (in decreasing order)
     * @param task
     * @return
     */
    @Override
    public int compareTo(Task task) {
        return Integer.compare(task.bottomLevel, this.bottomLevel);
    }

    public void setBottomLevel(int bottomLevel) {
        this.bottomLevel = bottomLevel;
    }

    public int getBottomLevel() {
        return bottomLevel;
    }

    /**
     * Returns true if and only if both tasks have the same name.
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object){
        if (!(object instanceof Task)){
            return false;
        } else {
            Task task = (Task)object;
            if (task.getName().equals(this.name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        return  name.hashCode();
    }

    /**
     * Returns the name as a number - may throw a NumberFormatException.
     * The client code using this class should handle a NumberFormatException in the case
     * where the names of the tasks are letters rather than numbers.
     * @return
     */
    public int getId() {
        return Integer.parseInt(name);
    }
}

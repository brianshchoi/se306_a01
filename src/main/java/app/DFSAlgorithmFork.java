package app;

import scheduleModel.*;
import taskModel.Task;
import taskModel.TaskModel;
import view.listeners.AlgorithmListener;
import view.listeners.AlgorithmObservable;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class DFSAlgorithmFork implements IAlgorithm, AlgorithmObservable {
    private TaskModel taskModel;
    private int numOfProcessors;

    private static ForkJoinPool pool;
    private static ISchedule bestSchedule;
    private static int bound = Integer.MAX_VALUE;

    private List<AlgorithmListener> listeners = new ArrayList<>();
    private int numberOfCores;

    /**
     * This class is used to parallelize our algorithm.  The non-parallelized version
     * of our algorithm can be found in DFSAlgorithm.java.  This class has to nest
     * the original class so that it can extend RecursiveAction and be passed to a
     * ForkJoinPool instance.
     * @param taskModel
     * @param numOfProcessors
     * @param numberOfCores
     */
    public DFSAlgorithmFork(TaskModel taskModel, int numOfProcessors, int numberOfCores) {
        this.taskModel = taskModel;
        this.numOfProcessors = numOfProcessors;
        this.numberOfCores = numberOfCores;
    }

    @Override
    public ISchedule run() {
        int depth = 0;
        Schedule schedule = new Schedule(numOfProcessors);
        List<Task> freeTasks = getFreeTasks(schedule, taskModel.getTasks());
        Set<Task> pTasks = new HashSet<>();

        // Create thread pool
        pool = new ForkJoinPool(numberOfCores);

        // Create task and start on thread from thread pool
        DFSAlgorithmTask task = new DFSAlgorithmTask(freeTasks, depth, schedule, pTasks, null, taskModel, listeners);
        pool.invoke(task); // Start the task (the RecursiveAction) and wait for it to be done

        // Inform GUI that algorithm is done
        fire(EventType.ALGORTHIM_FINISHED);
        return bestSchedule;
    }

    @Override
    public ISchedule getBestSchedule() {
        return bestSchedule;
    }

    // The DFS branch and bound algorithm.
    static class DFSAlgorithmTask extends RecursiveAction implements AlgorithmObservable {
        private List<Task> freeTasks;
        private int depth;
        private ISchedule schedule;
        private Set<Task> cleanPreviousTasks;
        private IProcessor previousProcessor;
        private Scheduler scheduler;
        private TaskModel taskModel;
        private List<AlgorithmListener> listeners;

        public DFSAlgorithmTask(List<Task> freeTasks, int depth, ISchedule schedule, Set<Task> cleanPreviousTasks, IProcessor pProc, TaskModel taskModel, List<AlgorithmListener> listeners) {
            this.taskModel = taskModel;
            this.freeTasks = freeTasks;
            this.depth = depth;
            this.schedule = schedule;
            this.previousProcessor = pProc;
            this.cleanPreviousTasks = cleanPreviousTasks;
            this.listeners = listeners;
            scheduler = new Scheduler();
        }

        // The task that gets scheduled on the thread.
        @Override
        protected void compute() {
            if (!freeTasks.isEmpty()) {
                // Get previous tasks
                Set<Task> previousTasks = new HashSet<>(cleanPreviousTasks);

                // Iterate through each task in this layer of tasks
                for (Task currentTask : freeTasks) {

                    // If the task we are currently scheduling
                    // has been already scheduled in the past after scheduling
                    // some other task in this layer, then this task has already been
                    // tried on all the processors.  There is no point trying this task
                    // on all the processors again - we should just stick them on the same
                    // processor as that is the only thing that can give us something different.
                    Set<IProcessor> processors = new HashSet<>();
                    if (previousTasks.contains(currentTask)) {
                        processors.add(previousProcessor);
                    } else {
                        processors.addAll(schedule.getProcessors()); // Otherwise we need to try it on all processors
                    }

                    // Try scheduling task on each processor and add copy to set of unique schedules
                    Set<ISchedule> schedules = new HashSet<>();
                    for (IProcessor currentProcessor : processors) {
                        scheduler.schedule(currentTask, currentProcessor, schedule);
                        try {
                            schedules.add((ISchedule) ((Schedule) schedule).clone());
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        scheduler.remove(currentTask, schedule);
                    }

                    // Remember that we have scheduled this task.
                    // When we come back from the recursive call and go onto the
                    // next currentTask, we know from previousTasks that THIS currentTask has been
                    // tried on all the processors.
                    previousTasks.add(currentTask);

                    // Stores which tasks will be pursued recursively in parallel with each other
                    List<DFSAlgorithmTask> tasks = new ArrayList<>();

                    // Go through each of the unique created schedules at this level
                    for (ISchedule currentSchedule : schedules) {
                        depth++;
                        fire(EventType.NUM_BRANCHES_CHANGED);

                        // Check if bad schedule
                        if (cost(currentSchedule) < bound) {
                            int numTasks = taskModel.getTasks().size();
                            if (depth == numTasks) { // Update the best schedule
                                bound = currentSchedule.getFinishTime();
                                try {
                                    bestSchedule = (ISchedule) ((Schedule) currentSchedule).clone();
                                    if (CLI.isVisualisation()) fire(EventType.BEST_SCHEDULE_UPDATED);
                                } catch (CloneNotSupportedException e){
                                    e.printStackTrace();
                                }
                            } else if (depth < numTasks) { // Keep building the schedule
                                // Set new list of tasks
                                List<Task> newFreeTasks = getFreeTasks(currentSchedule, taskModel.getTasks());
                                // Create next task
                                DFSAlgorithmTask dTask = new DFSAlgorithmTask(newFreeTasks, depth, currentSchedule, previousTasks, currentSchedule.getProcessorOf(currentTask), taskModel, listeners);
                                // Remember that we created this task
                                tasks.add(dTask);
                                // Do this task asynchronously
                                dTask.fork();
                            }
                        }
                        // Backtracking
                        depth--;
                    }
                    // Once all the baby schedules have been done, join the tasks together
                    for (DFSAlgorithmTask task : tasks){
                        task.join();
                    }
                }
            }
        }

        // Cost function
        private double cost(ISchedule schedule) {
            TreeSet<Double> costFunctionOutputs = new TreeSet<>();
            costFunctionOutputs.add(((double) schedule.f1()));
            costFunctionOutputs.add(schedule.f2(taskModel));
            return costFunctionOutputs.last();
        }

        @Override
        public void addAlgorithmListener(AlgorithmListener listener) {
            this.listeners.add(listener);
        }

        @Override
        public void removeAlgorithmListener(AlgorithmListener listener) {
            this.listeners.remove(listener);
        }

        // Fire events that need to be fired while the algorithm is running
        @Override
        public void fire(EventType eventType) {
            switch (eventType) {
                case BEST_SCHEDULE_UPDATED:
                    for (AlgorithmListener listener : listeners) {
                        listener.bestScheduleUpdated(bestSchedule);
                    }
                    break;
                case NUM_BRANCHES_CHANGED:
                    for (AlgorithmListener listener: listeners) {
                        listener.numberOfBranchesChanged();
                    }
                    break;
            }
        }
    }

    private static List<Task> getFreeTasks(ISchedule schedule, List<Task> allTasks) {
        List<Task> newFreeTasks = new ArrayList<>();

        // Create list of tasks which haven't been scheduled yet
        List<Task> scheduledTasks = schedule.getTasks();
        allTasks.removeAll(scheduledTasks);

        // Check if each unscheduled task's dependencies have been satisfied
        for (Task task : allTasks) {
            if (scheduledTasks.containsAll(task.getParents())) {
                newFreeTasks.add(task);
            }
        }
        Collections.sort(newFreeTasks);
        return newFreeTasks;
    }

    @Override
    public void addAlgorithmListener(AlgorithmListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeAlgorithmListener(AlgorithmListener listener) {
        listeners.remove(listener);
    }

    // Fire events that need to be scheduled once the algorithm is done
    @Override
    public void fire(AlgorithmObservable.EventType eventType) {
        if (!CLI.isVisualisation()) return;
        switch (eventType) {
            case ALGORTHIM_FINISHED:
                for (AlgorithmListener listener: listeners) {
                    listener.algorithmFinished();
                }
                break;
        }
    }
}

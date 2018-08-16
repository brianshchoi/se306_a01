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
    private IScheduler scheduler;
    private int numOfProcessors;

    private static ForkJoinPool pool;
    private static ISchedule bestSchedule; // Stores current best schedule
    private static int bound = Integer.MAX_VALUE;

    private int numBranches = 0;
    private List<AlgorithmListener> listeners = new ArrayList<>();
    private int numberOfCores;

    public DFSAlgorithmFork(TaskModel taskModel, int numOfProcessors, int numberOfCores) {
        this.taskModel = taskModel;
        this.numOfProcessors = numOfProcessors;
        this.numberOfCores = numberOfCores;
        scheduler = new Scheduler();
    }

    @Override
    public ISchedule run() {
        int depth = 0; // Stores depth of schedule
        Schedule schedule = new Schedule(numOfProcessors);
        List<Task> freeTasks = getFreeTasks(schedule, taskModel.getTasks());
        Set<Task> pTasks = new HashSet<>();

        pool = new ForkJoinPool(numberOfCores);
        DFSAlgorithmTask task = new DFSAlgorithmTask(freeTasks, depth, schedule, pTasks, null, taskModel, listeners);
        pool.invoke(task);

        fire(EventType.ALGORTHIM_FINISHED);
        return bestSchedule;
    }

    @Override
    public ISchedule getBestSchedule() {
        return bestSchedule;
    }

    static class DFSAlgorithmTask extends RecursiveAction implements AlgorithmObservable {

        private List<Task> freeTasks;
        private int depth;
        private ISchedule schedule;
        private Set<Task> cleanPreviousTasks;
        private IProcessor previousProcessor;
        private Scheduler scheduler;
        private TaskModel taskModel;
        private int numBranches = 0;
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

        @Override
        protected void compute() {
            if (!freeTasks.isEmpty()) {
                Set<Task> previousTasks = new HashSet<>(cleanPreviousTasks);
                for (Task currentTask : freeTasks) {
                    Set<IProcessor> processors = new HashSet<>();
                    if (previousTasks.contains(currentTask)) {
                        processors.add(previousProcessor);
                    } else {
                        processors.addAll(schedule.getProcessors());
                    }
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

                    previousTasks.add(currentTask);

                   // System.out.println(pool.getActiveThreadCount());
                    List<DFSAlgorithmTask> tasks = new ArrayList<>();
                    for (ISchedule currentSchedule : schedules) {
                        depth++;
                        fire(EventType.NUM_BRANCHES_CHANGED);
                        if (cost(currentSchedule) < bound) {
                            int numTasks = taskModel.getTasks().size();
                            if (depth == numTasks) {
                                bound = currentSchedule.getFinishTime();
                                try {
                                    bestSchedule = (ISchedule) ((Schedule) currentSchedule).clone();
                                    if (CLI.isVisualisation()) fire(EventType.BEST_SCHEDULE_UPDATED);
                                } catch (CloneNotSupportedException e){
                                    e.printStackTrace();
                                }
                            } else if (depth < numTasks) {
                                List<Task> newFreeTasks = getFreeTasks(currentSchedule, taskModel.getTasks());
                                DFSAlgorithmTask dTask = new DFSAlgorithmTask(newFreeTasks, depth, currentSchedule, previousTasks, currentSchedule.getProcessorOf(currentTask), taskModel, listeners);
                                tasks.add(dTask);
                                dTask.fork();
                            }
                        }
                        depth--;
                    }
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

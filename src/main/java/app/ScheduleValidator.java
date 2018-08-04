package app;

import app.exception.InvalidScheduleException;
import scheduleModel.IProcessor;
import scheduleModel.ISchedule;
import taskModel.Task;
import taskModel.TaskModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScheduleValidator {

    ISchedule schedule;

    public ScheduleValidator(ISchedule schedule) {
        this.schedule = schedule;
    }

    public void validate(TaskModel model) {
        // This validation is done on fully completed schedule

        // ********We need one more input: full task model in order to compare**********

        // ****Rules for valid schedule****
        // 1. All the tasks has been scheduled && No duplicate tasks

        // 2. No overlapping tasks on a single processor
        // 3. Nothing starts before time 0
        // 4. For all tasks all the parents has been schedule before a child

        // 5. Make sure communication link cost is taken into consideration
        //

        /* ## invalid tasks: #1
         Get tasks from each processors
         put them into a setS
         Make setT
         Compare the two sets*/

        // Putting entry set into String set

        List<Task> allTasks = model.getTasks();
        Set<String> allTaskSet = new HashSet<>();
        for(Task t : allTasks) {
            allTaskSet.add(t.getName());
        }

        // Get list of all tasks in string set
        List<IProcessor> processorList = schedule.getProcessors();
        List<Task> scheduledTasks = new ArrayList<>();
        for (int i = 0; i < processorList.size(); i++) {
            // Access each processor then add the tasks
            IProcessor processor = processorList.get(i);
            scheduledTasks.addAll(processor.getTasks());
        }
        // Putting list of task name into String set
        Set<String> scheduledTaskSet = new HashSet<>();
        for(Task t : scheduledTasks){
            scheduledTaskSet.add(t.getName());
        }

        if(!allTaskSet.equals(scheduledTaskSet)){
            throw new InvalidScheduleException("Invalid tasks scheduled (Duplicate tasks or not all tasks scheduled <3");
        }

        /*## Task scheduled before time 0: #3*/
        for (int i = 0; i < processorList.size(); i++) {
            List<Task> listOfTasks = processorList.get(i).getTasks();
            for(Task t : listOfTasks) {
                if(processorList.get(i).getStartTimeOf(t) < 0){
                    throw new InvalidScheduleException("Task scheduled before time 0 <3");
                }
            }
        }

        /*## overlapping schedule in a single processor: #2
        For each processor
        Make a new array with size equal to finish time of processor
        Initialise the all elements in array with 0
        Loop through all tasks in the map
        and for each task add 1 to the corresponding array element
        check if the completed array contains any index with number greater than 1*/
        for (int i = 0; i < processorList.size(); i++) {
            // if out of bounds exception plus one the array. I cant count
            int [] fill = new int[processorList.get(i).getFinishTime()];
            List<Task> listOfTasks = processorList.get(i).getTasks();
            for(Task t : listOfTasks) {
                int startTime = processorList.get(i).getStartTimeOf(t);

                // Maybe have to + or - 1 in the condition. I cant count
                for(int use = startTime; use < startTime + t.getWeight(); use++ ){
                    fill[use]++;
                }
            }
            // Check if the fill array has anything more than 1. I cant count
            for(int j = 0; j < fill.length; j++){
                if(fill[j] > 1){
                    throw new InvalidScheduleException("Overlapping tasks <3");
                }
            }

        }

        // ## parents scheduled after child: #4
        // for every task, loop through it's parents
        // check that for every parent, the finishing time of the parent is less than or equal to
        // the starting time of the child

        for (Task t: scheduledTasks) {
            if(!t.getParents().isEmpty()) {
                int start = 0;
                for (int i = 0; i < processorList.size(); i++) {
                    if (processorList.get(i).contains(t)) {
                        start = processorList.get(i).getStartTimeOf(t);
                    }
                }

                Set<Task> parentList = t.getParents();
                int parentFin;
                for(Task p : parentList){
                    for (int i = 0; i < processorList.size(); i++) {
                        if (processorList.get(i).contains(p)) {
                            parentFin = processorList.get(i).getFinishTimeOf(p);
                            if(start < parentFin){
                                throw new InvalidScheduleException("Parent task is not scheduled before child task <3");
                            }
                        }
                    }
                }
            }
        }

        // ## Make sure communication link cost is taken into consideration: #5

        for(Task p : scheduledTasks){
            if(!p.getChildren().isEmpty()){
                Set<Task> children = p.getChildren();

                for(Task c : children) {
                    IProcessor cProcessor = null;
                    IProcessor pProcessor = null;
                    for (IProcessor pr : processorList) {
                        if (pr.contains(c)) {
                            cProcessor = pr;
                        }
                        if(pr.contains(p)){
                            pProcessor = pr;
                        }
                    }
                    if(!pProcessor.equals(cProcessor)) {
                        int parentFin = pProcessor.getFinishTimeOf(p);
                        int childStart = cProcessor.getStartTimeOf(c);

                        // I cant count
                        if((childStart - parentFin) < p.getChildLinkCost(c)) {
                            throw new InvalidScheduleException("Link cost is ignored!! <3");
                        }
                    }
                }
            }
        }


    }
}

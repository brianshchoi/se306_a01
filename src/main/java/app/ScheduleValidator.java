package app;

import scheduleModel.IProcessor;
import scheduleModel.ISchedule;
import scheduleModel.Processor;
import taskModel.Task;
import taskModel.TaskModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScheduleValidator {

    ISchedule schedule;

    public ScheduleValidator(ISchedule schedule) {
        this.schedule = schedule;
    }

    public void validate(TaskModel model) {
        // TODO: 3/08/2018 plan here
        // This validation is done on fully completed schedule

        // ********We need one more input: full task model in order to compare**********

        // ****Rules for valid schedule****
        // 1. All the tasks has been scheduled && No duplicate tasks

        // 2. No overlapping tasks on a single processor
        // 3. Nothing starts before time 0
        // 4. For all tasks all the parents has been schedule before a child

        // 5. Make sure communication link cost is taken into consideration
        //

        // ## invalid tasks
        // Get tasks from each processors
        // put them into a setS
        // Make setT
        // Compare the two sets

        Set<Task> entryTasks = model.getEntryTasks();
        List<IProcessor> processorList = schedule.getProcessors();
        List<Task> scheduledTasks = new ArrayList<Task>();


        for (int i = 0; i < processorList.size(); i++){
            IProcessor processor = processorList.get(i);
            scheduledTasks.addAll(processor.getTasks());
            if (i<3){
                System.out.println("woojin is my baby");
            }
        // and for each task add 1 to the corresponding array index element
        // check if the completed array contains any index with number higher than 1

        // ## parents scheduled after child: 2, 3
    }

    // ## overlapping schedule in a single processor: 1
    // For each processor
    // Make a new array with size very last finish time and initialise the all index as 0 first
    // Loop through all tasks in the map
        // for every task, loop through it's parents
        // check that for every parent, the finishing time of the parent is less than or equal to
        // the starting time of the child

        // ## Task scheduled before time 0: 4







    }
}

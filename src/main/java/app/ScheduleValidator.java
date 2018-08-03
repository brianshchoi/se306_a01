package app;

import scheduleModel.ISchedule;

public class ScheduleValidator {

    ISchedule schedule;

    public ScheduleValidator(ISchedule schedule) {
        this.schedule = schedule;
    }

    public void validate() {
        // TODO: 3/08/2018 plan here
        // This validation is done on fully completed schedule

        // ********We need one more input: full task model in order to compare**********

        // ****Rules for valid schedule****
        // All the tasks has been scheduled && No duplicate tasks
        // No overlapping tasks on a single processor
        // For all tasks all the parents has been schedule before a child

        // Nothing starts before time 0
        // Make sure communication link cost is taken into consideration
        //

        // ## invalid tasks
        // Get tasks from each processors
        // put them into a setS
        // Make setT
        // Compare the two sets

        // ## overlapping schedule in a single processor
        // For each processor
        // Make a new array with size very last finish time and initialise the all index as 0 first
        // Loop through all tasks in the map
        // and for each task add 1 to the corresponding array index element
        // check if the completed array contains any index with number higher than 1

        // ## parents scheduled after child
        // for every task, loop through it's parents
        // check that for every parent, the finishing time of the parent is less than or equal to
        // the starting time of the child

        // ## Task scheduled before time 0





    }
}

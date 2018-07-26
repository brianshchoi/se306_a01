package schedule;

import model.Task;

import java.util.HashMap;
import java.util.Map;

public class Schedule {

    Map<Integer, Processor> processors = new HashMap<>();


    public int getMakeSpan() {


        return 0;
    }

    public void scheduleOnProcessor(int processor, Task task, int scheduleTime) {
        // TODO: 26/07/2018 Get the processor from the map by looking up using the processor num

        // TODO: 26/07/2018 invoke a method on the processor object we received that lets us schedule a task

        // TODO: 26/07/2018 pass in the task to be scheduled, and the time to schedule it at
    }



}
